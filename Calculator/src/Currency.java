public class Currency extends TopSuperClass {
    /**
     * Panel for the Currency child class
     */
    public Currency(){
        super("Standard", true, false);
        getSymbols("http://data.fixer.io/api/symbols?access_key=bbc38b91a26ea0f22b6cf96713b5ce0b", "EUR");
        getRates("http://data.fixer.io/api/latest?access_key=bbc38b91a26ea0f22b6cf96713b5ce0b");
    }
}
