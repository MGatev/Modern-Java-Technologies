public class JumpGame {
    public static boolean canWin(int[] array){
        int size = array.length;
        for(int i = 0; i < size - 1; i++){
            if(array[i] == 0 && i == 0)
                return false;

            if(array[i] == 0){
                int zeroesCount = 1, j = i + 1;
                while(j < size && array[j++] == 0)
                    zeroesCount++;

                boolean isNumber = false;
                for(int t = i - 1 ; t >= 0; t--){
                    if(array[t] >= zeroesCount + i - 1 - t ){
                        i += zeroesCount;
                        isNumber = true;
                        break;
                    }
                }

                if(!isNumber)
                    return false;
            }
            else{
                if (array[i] >= size - i + 1)
                    return true;
            }
        }
        return true;
    }
}
