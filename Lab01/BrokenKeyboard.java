public class BrokenKeyboard {
    static public int calculateFullyTypedWords(String message, String brokenKeys){
        String stripped = message.strip();
        String[] words = stripped.split(" ");
        int counter = 0;
        for(String i:words){
            if(i.length() == 0)
                continue;
            boolean canWrite = true;
            for(int j = 0; j < i.length(); j++){
                for(int k = 0; k < brokenKeys.length(); k++){
                    if(i.charAt(j) == brokenKeys.charAt(k)) {
                        canWrite = false;
                        break;
                    }
                }
                if(!canWrite)
                    break;
            }
            if(canWrite)
                counter++;
        }
        return counter;
    }
}
