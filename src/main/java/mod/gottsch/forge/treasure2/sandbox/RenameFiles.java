package mod.gottsch.forge.treasure2.sandbox;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RenameFiles {

	public static void main(String[] args) {
		List<String> materials = Arrays.asList("wood", "iron", "copper", "silver", "gold", "blood", "bone", /*"black", */"autium");
//		List<String> gems = Arrays.asList("amethyst", "topaz", "onyx", "diamond", "emerald", "ruby", "sapphire", "white_pearl", "black_pearl");
		List<String> gems = Arrays.asList("topaz");
		String path1 = "C:\\Development\\workspace\\git\\GealdorCraft-1.18.2\\src\\main\\resources\\assets\\gealdorcraft\\textures\\item\\jewelry";
		String path2 = "C:\\temp";
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path1))) {
			for (Path filePath : stream) {
				if (Files.isRegularFile(filePath, new LinkOption[] {})) {
					String filename = filePath.toString();
					if (filePath.getFileName().toString().endsWith(".png")) {
						// TODO test against gem + material combo
						// TODO replace with proper combo
						for (String gem : gems) {
							if (filename.contains(gem)) {
								for (String material : materials) {
									String combo = gem + "_" + material;
									if (filename.contains(combo)) {
										File file = filePath.toFile();
										String newFilename = filename.replace(combo, (material + "_" + gem));
										boolean flag = file.renameTo(new File(newFilename));
										  
								        // if renameTo() return true then if block is
								        // executed
								        if (flag == true) {
								            System.out.println("file Successfully Renamed to -> " + newFilename);
								        }
								        else {
								        	System.out.println("unable to rename file -> " + filename);
								        }
									}
								}
							}
						}
						
//						if (filename.contains("black_black_pearl") ) {//&& !filename.contains("black_pearl")) {
//							File file = filePath.toFile();
//							String newFilename = filename.replace("black_black_pearl", "shadow_black_pearl");
//							boolean flag = file.renameTo(new File(newFilename));
//							  
//					        // if renameTo() return true then if block is
//					        // executed
//					        if (flag == true) {
//					            System.out.println("file Successfully Renamed to -> " + newFilename);
//					        }
//					        else {
//					        	System.out.println("unable to rename file -> " + filename);
//					        }
//						}
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

}
