package com.example.demo.core;

import com.example.demo.dto.in.ShoeStockChange;
import com.example.demo.dto.out.ShopStock;

public interface StockCore {

  ShopStock getShopStock();

  void shoeStockChange(ShoeStockChange shoeStockChange);

}
