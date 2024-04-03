public class IPValidator {
    public static boolean validateIPv4Address(String str) {
        String[] buckets = str.split("\\.");
        if(buckets.length == 0)
            return false;

        for (int i = 0; i < buckets.length; i++){
            if((buckets.length != 4) || buckets[i].isEmpty() || buckets[i].length() > 3  || (buckets[i].length() == 2 && buckets[i].charAt(0) == '0') || Integer.parseInt(buckets[i]) < 0 || Integer.parseInt(buckets[i]) > 255)
                return false;
        }
        return true;
    }
}
