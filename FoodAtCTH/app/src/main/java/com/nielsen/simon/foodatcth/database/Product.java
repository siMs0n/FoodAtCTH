package com.nielsen.simon.foodatcth.database;

/**
 * Created by Simon on 2015-07-20.
 */
public class Product {

    private int _id;
    private String _productName;
    private String _productDescription;
    private int _price;

    public Product(){

    }

    public Product(int id, String productName, String productDescription, int price){
        if(id<1){
            throw new IllegalArgumentException("Id needs to be a postive integer");
        }else if(productName == null || productName.isEmpty()){
            throw new IllegalArgumentException("The product needs a name");
        }else if(price<0){
            throw new IllegalArgumentException("The price needs to be a postive integer");
        }
        _id = id;
        _productName = productName;
        _productDescription = productDescription;
        _price = price;
    }

    public int get_id() {
        return _id;
    }

    public String getProductName() {
        return _productName;
    }

    public String getProductDescription() {
        return _productDescription;
    }

    public int getPrice() {
        return _price;
    }
}
