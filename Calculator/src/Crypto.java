public class Crypto extends TopSuperClass {
    /**
     * Panel for the Crypto child class
     */
    public Crypto(){
        super("Standard", true, false);
        String symbolsURL = "https://rest.coinapi.io/v1/assets";
        String ratesURL = "https://rest.coinapi.io/v1/exchangerate/BTC/?apikey=B1A0F72C-5608-43E6-B8E5-82AE27C314EF";
        getCryptoSymbols(symbolsURL, ratesURL, "BTC");
    }
}
