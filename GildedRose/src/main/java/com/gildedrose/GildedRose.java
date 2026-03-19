package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void doAgedBrie(Item item) {
        if (item.quality < 50) {
            item.quality = item.quality + 1;
        }

        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            if (item.quality < 50) {
                item.quality = item.quality + 1;
            }
        }
    }

    public void doSulfuras(Item item){
        // ça change rien
    }

    public void doBackstagePasses(Item item) {
        if (item.quality < 50) {
            item.quality = item.quality + 1;

            if (item.sellIn < 11) {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }

            if (item.sellIn < 6) {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }

        }

        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            item.quality = 0;
        }
    }

    public void doNormalItem(Item item) {
            if (item.quality > 0) {
                    item.quality = item.quality - 1;
            }


            item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            if (item.quality > 0) {
                item.quality = item.quality - 1;
            }
        }
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            switch (items[i].name) {
                case "Aged Brie":
                    doAgedBrie(items[i]);
                    return;
                case "Backstage passes to a TAFKAL80ETC concert":
                    doBackstagePasses(items[i]);
                    return;
                case "Sulfuras, Hand of Ragnaros":
                    doSulfuras(items[i]);
                    return;
                default:
                    doNormalItem(items[i]);
                    return;
            }
        }
    }
}
