/**
 * MDS: Multi Dimension Search
 * @author Saurav Sharma (sxs179830)
 * @author Sudeep Maity (sdm170530)
 * @author Shiva Prasad Reddy Bitla (sxb180066)
 */

package sxs179830;

// If you want to create additional classes, place them in this file as subclasses of MDS

import java.util.*;

public class MDS {

    /**
     * Product class to store the information regarding the product.
     */
    public class Product {

        private long id;
        private List<Long> description;
        private Money price;

        Product(long id, Money price, List<Long> list) {
            this.id = id;
            this.price = price;
            this.description = list;
        }

        /**
         * @return Id of the product
         */
        public long getId() {
            return this.id;
        }

        /**
         * @return Price of the product
         */
        public Money getPrice() {
            return this.price;
        }

        /**
         * Update price of the product
         * @param price latest price of the product
         */
        public void setPrice(Money price) {
            this.price = price;
        }

        /**
         * @return Description i.e. list of long of the product
         */
        public List<Long> getDescription() {
            return this.description;
        }

        /**
         * Update the description of the product
         * @param list list of long to set for the product
         */
        public void setDescription(List<Long> list) {
            this.description = list;
        }
    }

    // Add fields of MDS here
    private Map<Long, Product> primaryIndexById;
    private Map<Long, Set<Long>> secondaryIndexByDescription;

    // Constructors
    public MDS() {
        primaryIndexById = new TreeMap<>();
        secondaryIndexByDescription = new HashMap<>();
    }


    /**
     * Insert(id,price,list): insert a new item whose description is given
     * in the list.  If an entry with the same id already exists, then its
     * description and price are replaced by the new values, unless list
     * is null or empty, in which case, just the price is updated.
     * Returns 1 if the item is new, and 0 otherwise.
     * @param id product id
     * @param price product price
     * @param list product description as long ints
     * @return 1 if product doesn't exists otherwise returns 0
     */
    public int insert(long id, Money price, List<Long> list) {
        int flag = 0;
        Product prod = primaryIndexById.get(id);
        if(prod != null) {
            if(list.size() != 0) {
                List<Long> desc = prod.getDescription();
                removeSecondaryIndex(id, desc);
                prod.setDescription(list);
                addSecondaryIndex(id, list);
            }
            prod.setPrice(price);
        } else {
            prod = new Product(id, price, list);
            primaryIndexById.put(id, prod);
            addSecondaryIndex(id, list);
            flag = 1;
        }
        return flag;
    }

    /**
     * Add secondary index for a Product with given description
     * @param id id of the Product
     * @param list description of product as list of long
     */
    private void addSecondaryIndex(long id, List<Long> list) {
        for(long d : list) {
            Set<Long> idList = secondaryIndexByDescription.get(d);
            if(idList == null) {
                idList = new TreeSet<>();
                idList.add(id);
            } else {
                idList.add(id);
            }
        }
    }

    /**
     * Remove the secondary index for a Product with given description
     * @param id id of the Product
     * @param list description of product as list of long
     */
    private void removeSecondaryIndex(long id, List<Long> list) {
        for(long d : list) {
            Set<Long> idList = secondaryIndexByDescription.get(d);
            if(idList != null && idList.size() > 0) {
                idList.remove(id);
            }
        }
    }

    /**
     * Find(id): return price of item with given id (or 0, if not found).
     * @param id product id to be searched
     * @return price of the product
     */
    public Money find(long id) {
        Product product = primaryIndexById.get(id);
        if(product != null) return product.getPrice();
        return new Money();
    }


    /**
     * Delete(id): delete item from storage.  Returns the sum of the
     * long ints that are in the description of the item deleted,
     * or 0, if such an id did not exist.
     * @param id id of the product to be deleted
     * @return return sum of the long ints in description if id exists otherwise returns 0
     */
    public long delete(long id) {
        Product productToDelete = primaryIndexById.get(id);
        if(productToDelete != null) {
            primaryIndexById.remove(id);
            long count = 0;
            for(long l : productToDelete.getDescription()) {
                count += l;
            }
            removeSecondaryIndex(id, productToDelete.getDescription());
            return count;
        }
        return 0;
    }

    /**
     * FindMinPrice(n): given a long int, find items whose description
     * contains that number (exact match with one of the long ints in the
     * item's description), and return lowest price of those items.
     * @param n long value in description
     * @return min price of the product with given description
     */
    public Money findMinPrice(long n) {
        Set<Long> idList = secondaryIndexByDescription.get(n);
        Money minPrice = null;
        if(idList != null && idList.size() > 0) {
            for(long l : idList) {
                Money newMoney = primaryIndexById.get(l).getPrice();
                if(minPrice == null) minPrice = newMoney;
                else if(minPrice.compareTo(newMoney) >= 0 ) {
                    minPrice = newMoney;
                }
            }
            return minPrice;
        }
        return new Money();
    }


    /**
     * FindMaxPrice(n): given a long int, find items whose description
     * contains that number, and return highest price of those items.
     * Return 0 if there is no such item.
     * @param n long value in description
     * @return max price of the product with given description
     */
    public Money findMaxPrice(long n) {
        Set<Long> idList = secondaryIndexByDescription.get(n);
        Money maxPrice = null;
        if(idList != null && idList.size() > 0) {
            for(long l : idList) {
                Money newMoney = primaryIndexById.get(l).getPrice();
                if(maxPrice == null) maxPrice = newMoney;
                else if(maxPrice.compareTo(newMoney) <= 0 ) {
                    maxPrice = newMoney;
                }
            }
            return maxPrice;
        }
        return new Money();
    }

    /*
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(long n, Money low, Money high) {
        return 0;
    }

    /*
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
    */
    public Money priceHike(long l, long h, double rate) {
        return new Money();
    }

    /*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    public long removeNames(long id, java.util.List<Long> list) {
        return 0;
    }

    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> {
        long d;  int c;
        public Money() { d = 0; c = 0; }
        public Money(long d, int c) { this.d = d; this.c = c; }
        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if(len < 1) { d = 0; c = 0; }
            else if(part.length == 1) { d = Long.parseLong(s);  c = 0; }
            else { d = Long.parseLong(part[0]);  c = Integer.parseInt(part[1]); }
        }
        public long dollars() { return d; }
        public int cents() { return c; }

        public int compareTo(Money other) {
            int cmp = Long.compare(this.dollars(), other.dollars());
            if(cmp == 0) {
                return Integer.compare(this.cents(), other.cents());
            } else {
                return cmp;
            }
        }
        public String toString() { return d + "." + c; }
    }

    public static void main(String[] args) {

    }
}
