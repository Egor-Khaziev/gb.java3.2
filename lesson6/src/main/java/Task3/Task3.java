package Task3;

public class Task3 {

    public boolean isFourAndIsOne(int[] arrays){
        boolean one = false;
        boolean four = false;
        for (int i: arrays) {
            if( i == 1) one = true;

            if (i == 4) four = true;
        }
        return one==true&&four==true;
    }

}
