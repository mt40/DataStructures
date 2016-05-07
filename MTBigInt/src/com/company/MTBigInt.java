package com.company;

public class MTBigInt implements Comparable<MTBigInt> {
    private final int INITIAL_LEN = 2;
    private static final int BASE = 10000;

    private int []words;

    public int length = 1; // Default value is 0

    public MTBigInt() {
        words = new int[INITIAL_LEN];
    }

    public MTBigInt(int len) {
        len = nearestPowerOf2(len);
        words = new int[len];
    }

    public MTBigInt add(MTBigInt other) {
        int maxLen = Math.max(length, other.length);
        MTBigInt sum = new MTBigInt(maxLen + 1);
        int carry = 0;
        for(int i = 0; i < maxLen; ++i) {
            sum.words[i] = words[i] + other.words[i] + carry;
            carry = sum.words[i] / BASE;
            sum.words[i] %= BASE;
        }
        if(carry > 0)
            sum.words[maxLen + 1] = carry;

        sum.truncate();
        return sum;
    }

    public MTBigInt multiply(MTBigInt other) {
        MTBigInt product = new MTBigInt(length + other.length);
        for(int i = 0; i < length; ++i) {
            int carry = 0;
            for(int j = 0; j < other.length; ++j) {
                int k = i + j;
                product.words[k] += words[i] * other.words[j] + carry;
                carry = product.words[k] / BASE;
                product.words[k] %= BASE;
            }
            if(carry > 0)
                product.words[i + other.length] += carry;
        }

        product.length = length + other.length;
        product.truncate();
        return product;
    }

    public MTBigInt subtract(MTBigInt other) {
        int maxLen = Math.max(length, other.length);
        MTBigInt rs = new MTBigInt(maxLen);
        int carry = 0;
        for(int i = 0; i < maxLen; ++i) {
            int otherDigit = (i < other.length) ? other.words[i] : 0;
            int dif = words[i] - otherDigit - carry;
            if(dif < 0) {
                dif += BASE;
                carry += BASE;
            }
            rs.words[i] = dif;
        }

        rs.truncate();
        return rs;
    }

    public MTBigInt divide(MTBigInt other) {
        MTBigInt tmp = new MTBigInt(length);
        MTBigInt rs = new MTBigInt(length);
        for(int i = length - 1, j = 0; i >= 0; --i, ++j) {
            tmp.append(words[i]);
            int low = 1, hi = BASE - 1;
            while(low <= hi) {
                int mid = (low + hi) >>> 1;
                MTBigInt bm = MTBigInt.valueOf(mid);
                if(tmp.compareTo(other.multiply(bm)) < 0)
                    hi = mid - 1;
                else
                    low = mid + 1;
            }
            rs.append(hi);
            tmp = tmp.subtract(other.multiply(MTBigInt.valueOf(hi)));
        }
        rs.truncate();
        return rs;
    }

    private void append(int val) {
        if(length + 1 >= words.length)
            grow();
        System.arraycopy(words, 0, words, 1, length);
        words[0] = val;
        length++;

        truncate();
    }

    private void grow() {
        int oldCap = words.length;
        int newCap = oldCap << 1;

        // transfer
        int []newArr = new int[newCap];
        System.arraycopy(words, 0, newArr, 0, length);
        words = newArr;
    }

    @Override
    public int compareTo(MTBigInt o) {
        if(length != o.length)
            return Integer.compare(length, o.length);
        for(int i = length - 1; i >= 0; --i)
            if(words[i] != o.words[i])
                return Integer.compare(words[i], o.words[i]);
        return 0;
    }

    /**
     * Fix value of length
     */
    private void truncate() {
        while(length > 0 && words[length - 1] == 0)
            length--;
    }

    private int nearestPowerOf2(int x) {
        if(x < INITIAL_LEN) return INITIAL_LEN;
        if(Integer.bitCount(x) == 1) // already a power of 2
            return x;
        return Integer.highestOneBit(x) << 1;
    }

    public static MTBigInt ZERO = new MTBigInt();

    public static MTBigInt valueOf(int val) {
        int len = (val == 0) ? 1 : 0;
        for(int i = val; i > 0; i /= BASE)
            len++;

        MTBigInt bi = new MTBigInt(len);
        int i;
        for(i = 0; val > 0; ++i, val /= BASE)
            bi.words[i] = val % BASE;
        bi.length = i;

        return bi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; ++i) {
            sb.insert(0, (i < length - 1) ? String.format("%04d", words[i]) : words[i]);
        }
        return sb.toString();
    }
}
