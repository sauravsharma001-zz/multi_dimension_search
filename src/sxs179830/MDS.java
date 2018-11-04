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
        private Set<Long> description;
        private Money price;

        Product(long id, Money price, Set<Long> list) {
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
        public Set<Long> getDescription() {
            return this.description;
        }

        /**
         * Update the description of the product
         * @param description list of long to set for the product
         */
        public void setDescription(Set<Long> description) {
            this.description = description;
        }
    }

    private Map<Long, Product> primaryIndexById;
    private Map<Long, Set<Long>> secondaryIndexByDescription;

    /**
     * Initializing Two Map used for indexing
     */
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
                Set<Long> desc = prod.getDescription();
                removeSecondaryIndex(id, desc);
                prod.setDescription(new HashSet<>(list));
                addSecondaryIndex(id, new HashSet<>(list));
            }
            prod.setPrice(price);
        } else {
            prod = new Product(id, price, new HashSet<>(list));
            primaryIndexById.put(id, prod);
            addSecondaryIndex(id, new HashSet<>(list));
            flag = 1;
        }
        return flag;
    }

    /**
     * Add secondary index for a Product with given description
     * @param id id of the Product
     * @param list description of product as list of long
     */
    private void addSecondaryIndex(long id, Set<Long> list) {
        for(long d : list) {
            Set<Long> idList = secondaryIndexByDescription.get(d);
            if(idList == null) {
                idList = new TreeSet<>();
                idList.add(id);
                secondaryIndexByDescription.put(d, idList);
            } else {
                idList.add(id);
                secondaryIndexByDescription.put(d, idList);
            }
        }
    }

    /**
     * Remove the secondary index for a Product with given description
     * @param id id of the Product
     * @param desc description of product as list of long
     */
    private void removeSecondaryIndex(long id, Set<Long> desc) {
        for(long d : desc) {
            Set<Long> idList = secondaryIndexByDescription.get(d);
            if(idList != null && idList.size() > 0) {
                idList.remove(id);
                secondaryIndexByDescription.put(d, idList);
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
                if(id == 17581620) {
                    System.out.print(l + "  " + count);
                }
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
                Money productPrice = primaryIndexById.get(l).getPrice();
                if(maxPrice == null) maxPrice = productPrice;
                else if(maxPrice.compareTo(productPrice) <= 0 ) {
                    maxPrice = productPrice;
                }
            }
            return maxPrice;
        }
        return new Money();
    }

    /**
     * FindPriceRange(n,low,high): given a long int n, find the number
     * of items whose description contains n, and in addition,
     * their prices fall within the given range, [low, high].
     * @param n value to search in description of product
     * @param low lower bound of price
     * @param high higher bound of price
     * @return number of product with price withing given range and containing given value in description
     */
    public int findPriceRange(long n, Money low, Money high) {
        Set<Long> idList = secondaryIndexByDescription.get(n);
        if(idList != null && idList.size() > 0) {
            int count = 0;
            for(long l : idList) {
                Money productPrice = primaryIndexById.get(l).getPrice();
                if(low.compareTo(productPrice) <= 0 && high.compareTo(productPrice) >= 0) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    /**
     * FindProductInRange(low,high):
     * @param low lower bound of id
     * @param high higher bound of id
     * @return list of product with id withing given range
     */
    public List<Long> FindProductIdsInRange(long low, long high){
        Set<Long> prodIds = primaryIndexById.keySet();
        List<Long> productIds = new ArrayList<>();
        if(!prodIds.isEmpty()){
            for(Long id : prodIds){
                if(id >= low && id <= high){
                    productIds.add(id);
                }
            }
        }
        return productIds;
    }

    /**
    *  g. PriceHike(l,h,r): increase the price of every product, whose id is
    *  in the range [l,h] by r%.  Discard any fractional pennies in the new
    *  prices of items.  Returns the sum of the net increases of the prices.
    */
    public Money priceHike(long l, long h, double rate) {
        Long sumHike = 0L;
        List<Long> prodIds = FindProductIdsInRange(l, h);
        for(Long id : prodIds){
            Product prod = primaryIndexById.get(id);
            Long actualPrice = prod.price.inCents();
            Long hike = (long)((actualPrice * rate) / 100);
            Long updatePrice = hike + actualPrice;
            long d = updatePrice/100;
            int c = (int)(updatePrice%100);
            Money updated =  new Money(d, c);
            prod.setPrice(updated);
            sumHike+= hike;
        }
        return new Money(sumHike/100, (int)(sumHike%100));
    }

    /**
     * RemoveNames(id, list): Remove elements of list from the description of id.
     * It is possible that some of the items in the list are not in the
     * id's description.  Return the sum of the numbers that are actually
     * deleted from the description of id.  Return 0 if there is no such id.
     * @param id product id
     * @param list list of long int to remove
     * @return count
     */
    public long removeNames(long id, List<Long> list) {
        Product prod = primaryIndexById.get(id);
        if(prod == null) return 0;
        long sum = 0;
        Set<Long> desc = prod.getDescription();
        Set<Long> valRemoved = new HashSet<>();
        Set<Long> set = new HashSet<>(list);
        for(long l : set) {
            if(desc.contains(l)) {
                valRemoved.add(l);
                sum += l;
            }
        }
        removeSecondaryIndex(id, valRemoved);
        return sum;
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
        public long inCents(){return (d*100)+c ; }
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
}
