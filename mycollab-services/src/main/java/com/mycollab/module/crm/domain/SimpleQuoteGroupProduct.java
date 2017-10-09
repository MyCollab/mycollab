package com.mycollab.module.crm.domain;

import java.util.List;

public class SimpleQuoteGroupProduct {
    private QuoteGroupProduct quoteGroupProduct;

    private List<Product> quoteProducts;

    public QuoteGroupProduct getQuoteGroupProduct() {
        return quoteGroupProduct;
    }

    public void setQuoteGroupProduct(QuoteGroupProduct quoteGroupProduct) {
        this.quoteGroupProduct = quoteGroupProduct;
    }

    public List<Product> getQuoteProducts() {
        return quoteProducts;
    }

    public void setQuoteProducts(List<Product> quoteProducts) {
        this.quoteProducts = quoteProducts;
    }
}
