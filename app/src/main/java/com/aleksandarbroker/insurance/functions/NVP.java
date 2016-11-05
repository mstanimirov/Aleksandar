/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aleksandarbroker.insurance.functions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * @author Martin
 */
public class NVP implements NameValuePair, Serializable {

    // serialization support
    private static final long serialVersionUID = 1L;
    private BasicNameValuePair nvp;

    public NVP(String name, String value) {
        nvp = new BasicNameValuePair(name, value);
    }

    @Override
    public String getName() {
        return nvp.getName();
    }

    @Override
    public String getValue() {
        return nvp.getValue();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(nvp.getName());
        out.writeUTF(nvp.getValue());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        nvp = new BasicNameValuePair(in.readUTF(), in.readUTF());
    }

    private void readObjectNoData() throws ObjectStreamException {
        // nothing to do
    }
}
