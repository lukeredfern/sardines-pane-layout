package com.redfern.luke.sardines;

import java.util.ArrayList;

public class Item {
    private double length;
    private double width;
    private double height;
    private int quantity;
    private int source;
    private int units;
    private Double[] position;
    private int productCode;
    private ArrayList<Item> subItems;
    private String description;
    private int color;



    public Item(double length, double width, double height, int quantity, int source, int units) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.source = source;
        this.units = units;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = 0;
        this.subItems = null;
        this.description = null;
    }
    public Item() {
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.quantity = 0;
        this.source = 0;
        this.units = 0;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = 0;
        this.subItems = null;
        this.description = null;
    }

    public Item(double length, double width, double height, int quantity, int source, int units, String description) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.source = source;
        this.units = units;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = 0;
        this.subItems = null;
        this.description = description;
    }

    public Item(int source) {
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.quantity = 0;
        this.source = source;
        this.units = 0;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = 0;
        this.subItems = null;
        this.description = null;
    }
    public Item(int source, int productCode, int quantity) {
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.quantity = quantity;
        this.source = source;
        this.units = 0;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = productCode;
        this.subItems = null;
        this.description = null;
    }

    public Item(int source, int productCode, int quantity, ArrayList<Item> subItems, String description) {
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.quantity = quantity;
        this.source = source;
        this.units = 0;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = productCode;
        this.subItems = subItems;
        this.description = description;
    }

    public Item(int source, int productCode, int quantity, double length, double width, double height, int units) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.source = source;
        this.units = units;
        this.position = new Double[]{0.0, 0.0, 0.0};
        this.productCode = productCode;
        this.subItems = null;
        this.description = null;
    }




    public void setLength(double length) {
        this.length = length;
    }
    public double getLength() {
        return length;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getWidth() {
        return width;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getHeight() {
        return height;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setSource(int source) {
        this.source = source;
    }
    public int getSource() {
        return source;
    }
    public void setUnits(int units) {
        // Convert all to meters then back to new unit
        switch (this.units) {
            case 0: this.length = this.length/100;
                this.width = this.width/100;
                this.height = this.height/100;
                break;
            case 1:
                break;
            case 2: this.length = this.length/1000;
                this.width = this.width/1000;
                this.height = this.height/1000;
                break;
            case 3: this.length = this.length/39.3700787;
                this.width = this.width/39.3700787;
                this.height = this.height/39.3700787;
                break;
        }
        this.units = units;
        switch (this.units) {
            case 0: this.length = this.length*100;
                this.width = this.width*100;
                this.height = this.height*100;
                break;
            case 1:
                break;
            case 2: this.length = this.length*1000;
                this.width = this.width*1000;
                this.height = this.height*1000;
                break;
            case 3: this.length = this.length*39.3700787;
                this.width = this.width*39.3700787;
                this.height = this.height*39.3700787;
                break;
        }

    }
    public int getUnits() {
        return units;
    }
    public String getItemString() {
        String itemString = Integer.toString(this.source) + ", "
                + Double.toString(this.quantity) + ": "
                + Double.toString(this.length) + "x"
                + Double.toString(this.width) + "x"
                + Double.toString(this.height) + ".Pos:"
                + Double.toString(this.position[0])+","
                + Double.toString(this.position[1])+","
                + Double.toString(this.position[2])+",";
        return itemString;
    }

    public Double[] getDimensions() {
        Double[]  dimensions = {this.length, this.width, this.height};
        return dimensions;
    }
    public void setDimensions(Double x, Double y, Double z) {
        this.length = x;
        this.width = y;
        this.height = z;
    }
    public void setDimensionsArray(Double[] dim) {
        this.length = dim[0];
        this.width = dim[1];
        this.height = dim[2];
    }
    public Double[] getPosition() {
        return position;
    }
    public void setPosition(Double x, Double y, Double z) {
        this.position = new Double[]{x, y, z};
    }
    public void setPositionArray(Double[] pos) {
        this.position = pos;
    }
    public double getPosition(int pos) {
        return position[pos];
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }
    public int getProductCode() {
        return productCode;
    }

    public void setSubItems(ArrayList<Item> subItems) {
        this.subItems = subItems;
    }
    public ArrayList<Item> getSubItems() {
        return subItems;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public int getSubItemNum(){
        if (subItems==null){
            return 0;
        } else {
            int numSubItems = 0;
            for (int i = 0; i < subItems.size(); i++) {
                numSubItems += subItems.get(i).getQuantity();
            }
            return numSubItems;
        }
    }

    public double getVolume() {
        return length*width*height;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
