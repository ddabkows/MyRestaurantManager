package restaurant;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import databaseparams.Categories;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.json.JSONObject;
import packettypes.TableValuesColumns;
import restaurant.content.ProductTypes;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    ReentrantLock tableLock = new ReentrantLock();
    private boolean isOpen = false;
    private int peopleCount;
    private boolean tableBusy = false;
    private final List<Product> starters = new ArrayList<>();
    private final List<Product> dishes = new ArrayList<>();
    private final List<Product> drinks = new ArrayList<>();
    private final List<Product> desserts = new ArrayList<>();

    public boolean isOpen() {return this.isOpen;}
    private float totalPrice = 0;
    private float taxPrice = 0;
    LineSeparator lineSeparator = new LineSeparator();
    Font font = FontFactory.getFont(FontFactory.COURIER, 11, BaseColor.BLACK);

    public boolean open(int peopleCountToSet) {
        if (tableLock.isLocked()) {
            return false;
        }  else if (tableBusy) {
            return false;
        } else if (peopleCountToSet == 0 && this.isOpen) {
            tableBusy = true;
            return true;
        } else if (peopleCountToSet > 0 && !this.isOpen) {
            tableBusy = true;
            tableLock.lock();
            try {
                this.isOpen = true;
                peopleCount = peopleCountToSet;
            } finally {
                tableLock.unlock();
            }
            return true;
        }
        return false;
    }

    public boolean set(int peopleCountToSet, JSONObject productsJSONobj) {
        if (tableLock.isLocked()) {
            return false;
        }
        else if (!this.isOpen) {
            return false;
        }
        else {
            tableLock.lock();
            try {
                setPeopleCount(peopleCountToSet);
                Iterator<String> keys = productsJSONobj.keys();
                starters.clear();
                dishes.clear();
                desserts.clear();
                drinks.clear();
                while (keys.hasNext()) {
                    String productName = keys.next();
                    JSONObject productSpecifics = productsJSONobj.getJSONObject(productName);
                    int productType = productSpecifics.getInt(TableValuesColumns.PRODUCTTYPE.toString());
                    double productPrice = productSpecifics.getDouble(TableValuesColumns.PRODUCTPRICE.toString());
                    String productComment = productSpecifics.getString(TableValuesColumns.PRODUCTCOMMENT.toString());
                    if (productType == ProductTypes.STARTERS.getType()) {
                        starters.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment, Categories.STARTERS.getTax()));
                    }
                    else if (productType == ProductTypes.DISHES.getType()) {
                        dishes.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment, Categories.URUMAKIS.getTax()));
                    }
                    else if (productType == ProductTypes.DESSERTS.getType()) {
                    desserts.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment, Categories.URUMAKIS.getTax()));
                    }
                    else if (productType == ProductTypes.DRINKS.getType()) {
                        drinks.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment, Categories.SOFTS.getTax()));
                    }
                }
            } finally {
                tableLock.unlock();
            }
            tableBusy = false;
            return true;
        }
    }

    private String getDoubleString(double doubleToString) {
        String doubleString = String.valueOf(doubleToString);
        int dotIndex = doubleString.indexOf('.');
        if (dotIndex == -1) doubleString = doubleString + ".00";
        int centStrings = doubleString.length() - doubleString.indexOf('.');
        if (centStrings == 2) {
            doubleString = doubleString + "0";
        }
        return doubleString;
    }

    public PdfPCell getProductsBillCell(double toRound, Font font) {
        toRound = toRound * 100; toRound = Math.round(toRound); toRound = toRound / 100;
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(getDoubleString(toRound) + "€", font));
        pdfPCell.setBorder(0);
        return pdfPCell;
    }

    public PdfPTable getPriceTable(Font font) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(0);
        float[] columnWidths = {2f, 20f, 20f, 20f}; table.setWidths(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell voidCell = new PdfPCell(new Paragraph("")); voidCell.setBorder(0);
        PdfPCell htva = new PdfPCell(new Paragraph("HTVA", font)); htva.setBorder(0);
        double htvaDouble = totalPrice - taxPrice;
        PdfPCell htvaValue = getProductsBillCell(htvaDouble, font);
        PdfPCell tva = new PdfPCell(new Paragraph("TVA", font)); tva.setBorder(0);
        double tvaDouble = taxPrice;
        PdfPCell tvaValue = getProductsBillCell(tvaDouble, font);
        PdfPCell tvac = new PdfPCell(new Paragraph("TVAC", font)); tvac.setBorder(0);
        double tvacDouble = totalPrice;
        PdfPCell tvacValue = getProductsBillCell(tvacDouble, font);
        table.addCell(voidCell); table.addCell(htva); table.addCell(tva); table.addCell(tvac);
        table.addCell(voidCell); table.addCell(htvaValue); table.addCell(tvaValue); table.addCell(tvacValue);
        return table;
    }

    public PdfPTable getCategoryPrices(List<Product> products, PdfPTable table, Font font) {
        for (Product product : products) {
            PdfPCell quantity = new PdfPCell(new Paragraph(String.valueOf(product.getQuantity()), font));
            quantity.setBorder(0); quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell name = new PdfPCell(new Paragraph("  " + product.getName(), font));
            name.setBorder(0); name.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell price = new PdfPCell(new Paragraph(getDoubleString(product.getQuantity() * product.getPrice()) + "€", font));
            price.setBorder(0); price.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(quantity);
            table.addCell(name);
            table.addCell(price);
            double productPrice = product.getPrice() * product.getQuantity();
            totalPrice += productPrice;
            taxPrice += productPrice * (product.getTax() / 100.0);
        }
        return table;
    }

    public PdfPTable getProductsToKitchen(List<Product> products, PdfPTable table, Font font) {
        for (Product product : products) {
            font.setStyle(Font.BOLD);
            PdfPCell name = new PdfPCell(new Paragraph(product.getName(), font));
            name.setBorder(0);
            PdfPCell quantity = new PdfPCell(new Paragraph(String.valueOf(product.getQuantity()), font));
            quantity.setBorder(0);
            font.setStyle(Font.NORMAL);
            PdfPCell blank = new PdfPCell(new Paragraph(""));
            blank.setBorder(0);
            PdfPCell comment = new PdfPCell(new Paragraph(product.getComment(), font));
            comment.setBorder(0);
            table.addCell(quantity);
            table.addCell(name);
            table.addCell(blank);
            table.addCell(comment);

        }
        return table;
    }

    private boolean printList(PDDocument documentToPrint, String printer) throws PrinterException {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
        PrintService chosen = null;
        if (ps.length > 0) {
            for (PrintService pss : ps) {
                if (pss.getName().contains(printer)) {
                    chosen = pss;
                }
            }
        }
        if (chosen == null) {
            return false;
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(chosen);
        job.setPageable(new PDFPageable(documentToPrint));
        job.print(patts);
        return true;
    }

    public void createDishPrintHeader(String tableName, Document document, DateTimeFormatter dtfInvoice) throws DocumentException {
        font.setSize(11);
        Paragraph paragraph = new Paragraph("Table: " + tableName, font);
        document.add(paragraph);
        document.add(new Chunk(lineSeparator));
        font.setSize(10);
        paragraph = new Paragraph(dtfInvoice.format(LocalDateTime.now()), font);
        document.add(paragraph);
        font.setSize(11);
    }

    public void addProductsToPrint(Document document, List<Product> products) throws DocumentException {
        document.add(new Chunk(lineSeparator));
        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(0);
        float[] columnsWidths = {6f, 30f}; table.setWidths(columnsWidths);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        font.setSize(12);
        table = getProductsToKitchen(products, table, font);
        document.add(table); document.add(new Paragraph("\n"));
    }

    public void addStartersToPrint(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph("Starters", font);
        document.add(paragraph);
        addProductsToPrint(document, starters);
        document.add(new Chunk(lineSeparator));
    }

    public void addDrinksToPrint(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph("Drinks", font);
        document.add(paragraph);
        addProductsToPrint(document, drinks);
        document.add(new Chunk(lineSeparator));
    }

    public void addDishesToPrint(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph("Dishes", font);
        document.add(paragraph);
        addProductsToPrint(document, dishes);
        document.add(new Chunk(lineSeparator));
    }

    public void createPrintList(int productsSize, Document document, ByteArrayOutputStream baos) throws FileNotFoundException, DocumentException {
        PdfWriter.getInstance(document, baos);

        document.setPageSize(new Rectangle(250, 200 + 20 * productsSize));
        document.open();
    }

    public boolean printFood(String tableName, String printer) {
        try {
            DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyy.MM.dd HH:mm:ss");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            int size = (int) ((getSize(starters) + getSize(dishes)) * 1.5);
            createPrintList(size, document, baos);
            createDishPrintHeader(tableName, document, dtfInvoice);
            addStartersToPrint(document);
            addDishesToPrint(document);
            document.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            PDDocument documentToPrint = PDDocument.load(bais);
            return printList(documentToPrint, printer);
        } catch (DocumentException | IOException | PrinterException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getSize(List<Product> products) {
        int size = products.size();
        for (Product product : products) {
            if (!Objects.equals(product.getComment(), "")) {
                size += 2;
            }
        }
        return size;
    }

    public boolean printDrinks(String tableName, String printer) {
        try {
            DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyy.MM.dd HH:mm:ss");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            int size = getSize(drinks);
            createPrintList(size, document, baos);
            createDishPrintHeader(tableName, document, dtfInvoice);
            addDrinksToPrint(document);
            document.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            PDDocument documentToPrint = PDDocument.load(bais);
            return printList(documentToPrint, printer);
        } catch (DocumentException | IOException | PrinterException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean printDishes(String tableName, String printer) {
        try {
            DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyy.MM.dd HH:mm:ss");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            int size = getSize(dishes);
            createPrintList(size, document, baos);
            createDishPrintHeader(tableName, document, dtfInvoice);
            addDishesToPrint(document);
            document.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            PDDocument documentToPrint = PDDocument.load(bais);
            return printList(documentToPrint, printer);
        } catch (DocumentException | IOException | PrinterException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean printStarters(String tableName, String printer) {
        try {
            DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int size = getSize(starters);
            createPrintList(size, document, baos);
            createDishPrintHeader(tableName, document, dtfInvoice);
            addStartersToPrint(document);
            document.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            PDDocument documentToPrint = PDDocument.load(bais);
            return printList(documentToPrint, printer);

        } catch (DocumentException | IOException | PrinterException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void createInvoicePDF(String tableName, int curInvoice) {
        DateTimeFormatter dtfTile = DateTimeFormatter.ofPattern("yyy_MM_dd HH_mm_ss");
        DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String now = dtfTile.format(LocalDateTime.now());
        String fileName = "bills/" + tableName + "_invoice_" + now + ".pdf";
        File newFile = new File(fileName);
        try {
            if (!newFile.createNewFile()) {
                throw new Exception("File not created");
            }
        } catch (Exception e) {e.printStackTrace();}
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            LineSeparator lineSeparator = new LineSeparator();
            document.setPageSize(new Rectangle(250, 270 + 13 * (starters.size() + dishes.size() + desserts.size() + drinks.size())));
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
            Chunk chunk = new Chunk("Narcyz Manager", font);
            document.add(new Paragraph(chunk));
            document.add(new Chunk(lineSeparator));
            font.setSize(10);
            chunk = new Chunk(new VerticalPositionMark());
            Paragraph paragraph = new Paragraph(dtfInvoice.format(LocalDateTime.now()), font);
            paragraph.add(new Chunk(chunk));
            paragraph.add(String.valueOf(curInvoice));
            document.add(paragraph);
            document.add(new Chunk(lineSeparator));
            font.setSize(11);
            paragraph = new Paragraph("TABLE:  " + tableName, font);
            paragraph.add(new Chunk(chunk));
            paragraph.add("Guest(s): " + peopleCount);
            document.add(paragraph);
            font.setSize(7);
            document.add(new Chunk(lineSeparator));
            PdfPTable table = new PdfPTable(3);
            table.setSpacingBefore(0); table.setWidthPercentage(100);
            float[] columnWidths = {6f, 32f, 12f}; table.setWidths(columnWidths);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table = getCategoryPrices(starters, table, font);
            table = getCategoryPrices(dishes, table, font);
            table = getCategoryPrices(desserts, table, font);
            table = getCategoryPrices(drinks, table, font);
            document.add(table);
            font.setSize(9);
            paragraph = new Paragraph("Total:   " + getDoubleString(totalPrice) + "€", font);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
            document.add(new Chunk(lineSeparator));
            document.add(getPriceTable(font));

            document.close();
        } catch (Exception e) {e.printStackTrace();}
    }

    public boolean close(String tableName, int curInvoice) {
        if (this.isOpen && !this.tableLock.isLocked()) {
            createInvoicePDF(tableName, curInvoice);
            this.starters.clear();
            this.dishes.clear();
            this.desserts.clear();
            this.drinks.clear();
            this.isOpen = false;
            this.tableBusy = false;
            this.totalPrice = 0;
            this.taxPrice = 0;
            return true;
        } else {
            return false;
        }
    }
    public int getPeopleCount() {return this.peopleCount;}
    public void setPeopleCount(int peopleCountToSet) {this.peopleCount = peopleCountToSet;}
    public List<Product> getStarters() {return this.starters;}
    public List<Product> getDishes() {return this.dishes;}
    public List<Product> getDrinks() {return this.drinks;}
    public List<Product> getDesserts() {return this.desserts;}

    public boolean getTableBusy() {return this.tableBusy;}
    public void unbusyTable() {this.tableBusy = false;}
}
