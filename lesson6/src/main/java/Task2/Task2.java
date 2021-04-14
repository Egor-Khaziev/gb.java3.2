package Task2;

public class Task2 {


    public int[] lastFour(int[] arrays){
        int i = -1;

        for (int num = 0; num< arrays.length;num++) {
            if (arrays[num]==4){
                i=num;
            }
        }
        if (i==-1||i==arrays.length-1){return null;}

        int[]result = new int[arrays.length-(i+1)];

            for (int j = i+1, o = 0;j<arrays.length;j++,o++){
                result[o]=arrays[j];
            }

        return result;
    }


}
