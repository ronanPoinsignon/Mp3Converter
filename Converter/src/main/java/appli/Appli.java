package appli;

public class Appli {

	public static void main(String[] args) {
		AppliFx.mainFx(args);
		/*int[] illegalChars = {34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 47};
		Arrays.sort(illegalChars);
		StringBuilder cleanName = new StringBuilder();
		File fichier = new File("C:\\Users\\ronan\\Desktop\\ronan\\testMp4\\mp4H\\Arctic Monkeys - Do I Wanna Know? (Official Video).mp4");
	    for (int i = 0; i < fichier.getPath().length(); i++) {
	        int c = (int)fichier.getPath().charAt(i);
	        if (Arrays.binarySearch(illegalChars, c) < 0 || ((fichier.getPath().charAt(i - 1) + "").matches("[A-Z]") && c == ':')) {
	            cleanName.append((char)c);
	        }
	    }
	    System.out.println(cleanName.toString());*/
	}
}
