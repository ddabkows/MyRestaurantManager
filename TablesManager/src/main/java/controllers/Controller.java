package controllers;


import client.ClientSocketBuilder;


public class Controller {
    private ClientSocketBuilder clientSocket;

    public void setClientSocket(ClientSocketBuilder clientSocketToSet) {this.clientSocket = clientSocketToSet;}

    public ClientSocketBuilder getClientSocket() {return this.clientSocket;}
}
