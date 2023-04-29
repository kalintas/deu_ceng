public class PhoneNumber {

    private String countryCode;
    private String cityCode;
    private String number;

    public PhoneNumber(String phoneNumber) {

        this.countryCode = phoneNumber.substring(0, 3);
        this.cityCode = phoneNumber.substring(3, 6);
        this.number = phoneNumber.substring(6);
    }

    /**
     * Override toString() function to make PhoneNumber class printable
     * @return Returns the phone number. For example +90 (232) 1112233
     */
    @Override
    public String toString() {
        return String.format("%s (%s) %s", this.countryCode, this.cityCode, this.number);
    }


}
