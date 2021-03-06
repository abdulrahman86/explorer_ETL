package aion.dashboard.domainobject;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import static aion.dashboard.util.Utils.approximate;
import static aion.dashboard.util.Utils.getZDT;

public class TokenTransfers {

    private String operator;
    private String toAddress;
    private String fromAddress;
    private String contractAddress;
    private BigDecimal scaledValue;
    private String transactionHash;
    private String rawValue;
    private int tokendecimal;
    private BigDecimal granularity;
    private long blockNumber;
    private long transferTimestamp;
    private int blockYear;
    private int blockMonth;
    private int blockDay;
    private double approxValue;


    // linting rule squid:S00107 can be ignored here since the constructor is only accessed via the builder method
    private TokenTransfers(String operator, String toAddress, String fromAddress, BigDecimal value, String contractAddress, String transactionHash, String rawValue, int tokendecimal, BigDecimal granularity, long blockNumber, long transferTimestamp) {
        this.operator = operator;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.scaledValue = value;
        this.contractAddress = contractAddress;
        this.transactionHash = transactionHash;
        this.rawValue = rawValue;
        this.tokendecimal = tokendecimal;
        this.granularity = granularity;
        this.blockNumber = blockNumber;
        this.transferTimestamp = transferTimestamp;
        this.approxValue = approximate(new BigDecimal(rawValue), 18);

        ZonedDateTime zdt = getZDT(transferTimestamp);
        blockDay = zdt.getDayOfMonth();
        blockMonth = zdt.getMonthValue();
        blockYear = zdt.getYear();
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getRawValue() {
        return rawValue;
    }

    public int getTokendecimal() {
        return tokendecimal;
    }

    public BigDecimal getGranularity() {
        return granularity;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public BigDecimal getScaledValue() {
        return scaledValue;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public long getTransferTimestamp() {
        return transferTimestamp;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenTransfers)) return false;
        TokenTransfers tokenTransfers = (TokenTransfers) o;
        return getTransactionHash() == tokenTransfers.getTransactionHash() &&
                getBlockNumber() == tokenTransfers.getBlockNumber() &&
                getTransferTimestamp() == tokenTransfers.getTransferTimestamp() &&
                Objects.equals(getToAddress(), tokenTransfers.getToAddress()) &&
                Objects.equals(getFromAddress(), tokenTransfers.getFromAddress()) &&
                Objects.equals(getScaledValue(), tokenTransfers.getScaledValue()) &&
                Objects.equals(getContractAddress(), tokenTransfers.getContractAddress()) &&
                Objects.equals(getOperator(), tokenTransfers.getOperator());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToAddress(), getFromAddress(), getScaledValue(), getContractAddress(), getTransactionHash(), getBlockNumber(), getTransferTimestamp(), getFromAddress());
    }
    public int getBlockYear() {
        return blockYear;
    }


    public int getBlockMonth() {
        return blockMonth;
    }

    public int getBlockDay() {
        return blockDay;
    }

    public double getApproxValue() {
        return approxValue;
    }

    public static class TransferBuilder {
        private String operator;
        private String toAddress;
        private String fromAddress;
        private BigDecimal tokenValue;
        private String contractAddress;
        private String transactionHash;
        private long blockNumber;
        private long transactionTimestamp;
        private String rawValue;
        private int tokendecimal;
        private BigDecimal granularity;

        public TransferBuilder setRawValue(String rawValue) {
            this.rawValue = rawValue;
            return this;
        }

        public TransferBuilder setTokendecimal(int tokendecimal) {
            this.tokendecimal = tokendecimal;
            return this;
        }

        public TransferBuilder setGranularity(BigDecimal granularity) {
            this.granularity = granularity;
            return this;
        }


        public TransferBuilder setToAddress(String toAddress) {
            this.toAddress = toAddress;
            return this;
        }

        public TransferBuilder setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public TransferBuilder setScaledTokenValue(BigDecimal tokenValue) {
            this.tokenValue = tokenValue;
            return this;
        }

        public TransferBuilder setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
            return this;
        }

        public TransferBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public TransferBuilder setBlockNumber(long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public TransferBuilder setTransactionTimestamp(long transactionTimestamp) {
            this.transactionTimestamp = transactionTimestamp;
            return this;
        }


        public TokenTransfers build() {
            return new TokenTransfers(operator, toAddress, fromAddress, tokenValue, contractAddress, transactionHash, rawValue, tokendecimal, granularity, blockNumber, transactionTimestamp);
        }

        public TransferBuilder setOperator(String operator) {
            this.operator = operator;
            return this;
        }
    }
}
