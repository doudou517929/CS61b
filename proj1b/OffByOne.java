/*A class for off-by-1 comparators.*/
public class OffByOne implements CharacterComparator{

    /**  returns true for characters that are different by exactly one.*/
    @Override
    public boolean equalChars(char x, char y){
        if(Math.abs(x - y) == 1){
            return true;
        }
        return false;
    }

}
