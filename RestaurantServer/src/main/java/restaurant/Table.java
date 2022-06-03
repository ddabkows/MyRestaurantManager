package restaurant;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

    public PdfPTable getPriceTable() throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(0);
        float[] columnWidths = {2f, 4f, 4f, 4f}; table.setWidths(columnWidths);
        table.setWidthPercentage(40);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell voidCell = new PdfPCell(new Paragraph("")); voidCell.setBorder(0);
        PdfPCell htva = new PdfPCell(new Paragraph("HTVA")); htva.setBorder(0);
        PdfPCell htvaValue = new PdfPCell(new Paragraph(totalPrice - taxPrice + "€")); htvaValue.setBorder(0);
        PdfPCell tva = new PdfPCell(new Paragraph("TVA")); tva.setBorder(0);
        PdfPCell tvaValue = new PdfPCell(new Paragraph(taxPrice + "€")); tvaValue.setBorder(0);
        PdfPCell tvac = new PdfPCell(new Paragraph("TVAC")); tvac.setBorder(0);
        PdfPCell tvacValue = new PdfPCell(new Paragraph(totalPrice + "€")); tvacValue.setBorder(0);
        table.addCell(voidCell); table.addCell(htva); table.addCell(tva); table.addCell(tvac);
        table.addCell(voidCell); table.addCell(htvaValue); table.addCell(tvaValue); table.addCell(tvacValue);
        return table;
    }

    public PdfPTable getCategoryPrices(List<Product> products, PdfPTable table) {
        for (Product product : products) {
            PdfPCell quantity = new PdfPCell(new Paragraph(String.valueOf(product.getQuantity())));
            quantity.setBorder(0);
            PdfPCell name = new PdfPCell(new Paragraph(product.getName()));
            name.setBorder(0);
            PdfPCell price = new PdfPCell(new Paragraph(product.getQuantity() * product.getPrice() + "€"));
            price.setBorder(0);
            table.addCell(quantity);
            table.addCell(name);
            table.addCell(price);
            double productPrice = product.getPrice() * product.getQuantity();
            totalPrice += productPrice;
            taxPrice += productPrice * (product.getTax() / 100.0);
        }
        return table;
    }

    public PdfPTable getProductsToKitchen(List<Product> products, PdfPTable table) {
        for (Product product : products) {
            PdfPCell name = new PdfPCell(new Paragraph(product.getName()));
            name.setBorder(0);
            PdfPCell quantity = new PdfPCell(new Paragraph(String.valueOf(product.getQuantity())));
            quantity.setBorder(0);
            table.addCell(name);
            table.addCell(quantity);
        }
        return table;
    }

    private boolean printList(PDDocument documentToPrint) throws PrinterException {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
        PrintService chosen = null;
        if (ps.length > 0) {
            for (PrintService pss : ps) {
                if (pss.getName().contains("EPSON")) {
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

    public boolean printStarters(String tableName) {
        try {
            DateTimeFormatter dtfInvoice = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
            Chunk chunk = new Chunk("Table: " + tableName);
            document.add(chunk); document.add(new Paragraph("\n"));
            chunk = new Chunk("_____________________________\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            chunk = new Chunk(dtfInvoice.format(LocalDateTime.now()), font);
            document.add(chunk); document.add(new Paragraph("\n"));
            font.setSize(30);
            chunk = new Chunk("Starters");
            document.add(chunk); document.add(new Paragraph("\n"));
            chunk = new Chunk("_____________________________\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(2);
            table.setSpacingBefore(0);
            float[] columnsWidths = {7f, 3f}; table.setWidths(columnsWidths);
            table.setWidthPercentage(40);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table = getProductsToKitchen(starters, table);
            document.add(table); document.add(new Paragraph("\n"));
            document.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            PDDocument documentToPrint = PDDocument.load(bais);
            return printList(documentToPrint);

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
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 30, BaseColor.BLACK);
            Chunk chunk = new Chunk("Narcyz\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            font.setSize(12);
            chunk = new Chunk(dtfInvoice.format(LocalDateTime.now()) + "         " + curInvoice + "\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            chunk = new Chunk("_____________________________\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            font.setSize(20);
            chunk = new Chunk("TABLE       " + tableName + "      " + "Guest(s): " + peopleCount);
            document.add(chunk); document.add(new Paragraph("\n"));
            font.setSize(12);
            chunk = new Chunk("_____________________________\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(3);
            table.setSpacingBefore(0);
            float[] columnWidths = {2f, 7f, 3f}; table.setWidths(columnWidths);
            table.setWidthPercentage(40);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table = getCategoryPrices(starters, table);
            table = getCategoryPrices(dishes, table);
            table = getCategoryPrices(desserts, table);
            table = getCategoryPrices(drinks, table);
            document.add(table); document.add(new Paragraph("\n"));
            chunk = new Chunk("                     Total:   " + totalPrice + "€");
            document.add(chunk); document.add(new Paragraph("\n"));
            chunk = new Chunk("_____________________________\n", font);
            document.add(chunk); document.add(new Paragraph("\n"));
            document.add(getPriceTable()); document.add(new Paragraph("\n"));

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
