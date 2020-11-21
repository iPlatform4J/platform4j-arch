package com.platform4j.arch.domain;

import java.math.BigDecimal;
import java.util.Currency;

public class Money extends BaseObject implements Comparable {

    public static final String DEFAULT_CURRENCY_CODE = "CNY";

    public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    private static final int[] centFactors = new int[]{1, 10, 100, 1000};

    private long cent;

    private Currency currency;

    public Money() { }

    public Money(long yuan, int cent , String currencyCode) {
        this(yuan, cent, Currency.getInstance(currencyCode));
    }

    public Money(long yuan, int cent, Currency currency) {
        this.currency = currency;

        this.cent = (yuan * getCentFactor()) + (cent % getCentFactor());
    }

    public Money(String amount , String currencyCode) {
        this(amount, Currency.getInstance(currencyCode));
    }

    public Money(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    public Money(String amount,  String currencyCode, int roundingMode) {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode), roundingMode);
    }

    public Money(double amount , String currencyCode) {
        this(amount, Currency.getInstance(currencyCode));
    }

    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.cent = Math.round(amount * getCentFactor());
    }

    public Money(BigDecimal amount,String currencyCode)
    {
        this(amount, Currency.getInstance(currencyCode));
    }

    public Money(BigDecimal amount, int roundingMode) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE), roundingMode);
    }

    public Money(BigDecimal amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING_MODE);
    }

    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency = currency;
        this.cent = rounding(amount.movePointRight(currency.getDefaultFractionDigits()),
                roundingMode);
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(this.cent, this.currency.getDefaultFractionDigits());
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.cent = rounding(amount.movePointRight(2), BigDecimal.ROUND_HALF_EVEN);
        }
    }

    public long getCent() {
        return this.cent;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public String getCurrencyCode() {
        return this.currency.getCurrencyCode();
    }

    public int getCentFactor() {
        return centFactors[this.currency.getDefaultFractionDigits()];
    }

    public boolean equals(Object other) {
        return (other instanceof Money) && equals((Money) other);
    }

    public boolean equals(Money other) {
        return this.currency.equals(other.currency) && (this.cent == other.cent);
    }

    public int hashCode() {
        return (int) (this.cent ^ (this.cent >>> 32));
    }

    public int compareTo(Object other) {
        return compareTo((Money) other);
    }

    public int compareTo(Money other) {
        assertSameCurrencyAs(other);

        if (this.cent < other.cent) {
            return -1;
        } else if (this.cent == other.cent) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public Money add(Money other) {
        assertSameCurrencyAs(other);

        return newMoneyWithSameCurrency(this.cent + other.cent);
    }

    public Money addTo(Money other) {
        assertSameCurrencyAs(other);

        this.cent += other.cent;

        return this;
    }

    public Money subtract(Money other) {
        assertSameCurrencyAs(other);

        return newMoneyWithSameCurrency(this.cent - other.cent);
    }

    public Money subtractFrom(Money other) {
        assertSameCurrencyAs(other);

        this.cent -= other.cent;

        return this;
    }

    public Money multiply(long val) {
        return newMoneyWithSameCurrency(this.cent * val);
    }

    public Money multiplyBy(long val) {
        this.cent *= val;

        return this;
    }

    public Money multiply(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent * val));
    }

    public Money multiplyBy(double val) {
        this.cent = Math.round(this.cent * val);

        return this;
    }

    public Money multiply(BigDecimal val) {
        return multiply(val, DEFAULT_ROUNDING_MODE);
    }

    public Money multiplyBy(BigDecimal val) {
        return multiplyBy(val, DEFAULT_ROUNDING_MODE);
    }

    public Money multiply(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);

        return newMoneyWithSameCurrency(rounding(newCent, roundingMode));
    }

    public Money multiplyBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).multiply(val);

        this.cent = rounding(newCent, roundingMode);

        return this;
    }

    public Money divide(double val) {
        return newMoneyWithSameCurrency(Math.round(this.cent / val));
    }

    public Money divideBy(double val) {
        this.cent = Math.round(this.cent / val);

        return this;
    }

    public Money divide(BigDecimal val) {
        return divide(val, DEFAULT_ROUNDING_MODE);
    }

    public Money divide(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);

        return newMoneyWithSameCurrency(newCent.longValue());
    }

    public Money divideBy(BigDecimal val) {
        return divideBy(val, DEFAULT_ROUNDING_MODE);
    }

    public Money divideBy(BigDecimal val, int roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(this.cent).divide(val, roundingMode);

        this.cent = newCent.longValue();

        return this;
    }

    public Money[] allocate(int targets) {
        Money[] results = new Money[targets];

        Money lowResult = newMoneyWithSameCurrency(this.cent / targets);
        Money highResult = newMoneyWithSameCurrency(lowResult.cent + 1);

        int remainder = (int) this.cent % targets;

        for (int i = 0; i < remainder; i++) {
            results[i] = highResult;
        }

        for (int i = remainder; i < targets; i++) {
            results[i] = lowResult;
        }

        return results;
    }

    public Money[] allocate(long[] ratios) {
        Money[] results = new Money[ratios.length];

        long total = 0;

        for (long ratio : ratios) {
            total += ratio;
        }

        long remainder = this.cent;

        for (int i = 0; i < results.length; i++) {
            results[i] = newMoneyWithSameCurrency((this.cent * ratios[i]) / total);
            remainder -= results[i].cent;
        }

        for (int i = 0; i < remainder; i++) {
            results[i].cent++;
        }

        return results;
    }

    public String toString() {
        return getAmount().toString();
    }

    protected void assertSameCurrencyAs(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    protected long rounding(BigDecimal val, int roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    protected Money newMoneyWithSameCurrency(long cent1) {
        Money money = new Money(0, this.currency);

        money.cent = cent1;

        return money;
    }

    public void setCent(long cent) {
        this.cent = cent;
    }

    public void setCurrency(String currency) {
        this.currency = Currency.getInstance(currency);
    }
}
