package edu.whu.bookshop.dbtool.data;

public class bookBuilder {
    private book book_init=new book();

    public bookBuilder setID(int id) {
        book_init.id = id;
        return this;
    }

    public bookBuilder setName(String name) {
        book_init.name = name;
        return this;
    }

    public bookBuilder setPublisher(String publisher) {
        book_init.publisher = publisher;
        return this;
    }

    public bookBuilder setImgPath(String imgPath) {
        book_init.imgPath = imgPath;
        return this;
    }

    public bookBuilder setInfo(String info) {
        book_init.info = info;
        return this;
    }

    public bookBuilder setStoreCount(int storeCount) {
        book_init.storeCount = storeCount;
        return this;
    }

    public book getBook() {
        return book_init;
    }
}
