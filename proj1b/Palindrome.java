/*A class for palindrome operations.*/
public class Palindrome {
    //extends LinkedListDeque<T>
    private Deque<Character> str;

    /** Given a String, wordToDeque should return a Deque
     * where the characters appear in the same order as in the String.*/
    public Deque<Character> wordToDeque(String word){
        str = new LinkedListDeque<>();
        int l = word.length();
        for (int i = 0; i < l; i++) {
            str.addLast(word.charAt(i));
        }
        return str;
    }

    /** The isPalindrome method should return true if the given word is a palindrome,
     * and false otherwise. A palindrome is defined as a word that is the same
     * whether it is read forwards or backwards. */
    public boolean isPalindrome(String word){
        wordToDeque(word);
        int l = word.length();
        int i = 0;
        while (i < l / 2) {
            if(word.charAt(i) != word.charAt(l - i - 1)) {
                return false;
            }
            i += 1;
        }
        return true;
    }

    /** The method will return true if the word is a palindrome
     * according to the character comparison test provided by
     * the CharacterComparator passed in as argument cc.
     */
    public boolean isPalindrome(String word, CharacterComparator cc){
        int l = word.length();
        int i = 0;
        while(i < l / 2){
            if(!cc.equalChars(word.charAt(i), word.charAt(l - i - 1))){
                return false;
            }
            i += 1;
        }
        return true;
    }
}
