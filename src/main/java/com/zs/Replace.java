package com.zs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author zs
 * 	用指定的字典dict中声明的关键字， 替换指定路径下的文件、文件夹、文件内容中的关键字
 */
public class Replace {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	 * 默认的文件过滤器
	 */
	private static FileFilter defaultFileFilter = (f)->{
		String fileName = f.getName();
		if(f.isFile()) {
			if(fileName.endsWith(".java") || 
					fileName.endsWith("Mapper.xml")||
					fileName.endsWith(".properties")||
					fileName.endsWith(".yml")) {
				return true;
			}else {
				return false;
			}
		}
		return true;
	};
	
	/**
	 * 默认的字典
	 */
	private static HashMap<String,String> dict = new HashMap<String,String>();
	
	static {
//		dict.put("eps", "sahp");
//		dict.put("Eps", "Sahp");
//		dict.put("EPS", "SAHP");
		dict.put("sahp", "eps");
	}
	
	
	public static void main(String[] args) throws IOException {
		String realpath = "C:\\Users\\user\\Documents\\workspace-sts-3.9.4.RELEASE\\SAHP-new\\src\\main\\java\\com\\ebill\\sahp\\entity\\mapper\\business";
		replace(new File(realpath));
	}
	
	
	public static void replace (File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles(defaultFileFilter());
			for (File sbufile : files) {
				replace(sbufile);
			}
			String dirName = file.getName();
			String absolutePath = file.getAbsolutePath();
			System.err.println("路径："+absolutePath + "，目录名：" + dirName);
			//为文件夹重命名
			renameDir(file);
		}else if(file.isFile()){
			String dirName = file.getName();
			String absolutePath = file.getAbsolutePath();
			//重命名之后的新的file对象
			File newFile = renameFile(file);
			System.err.println("路径："+absolutePath + "，文件名：" + dirName);
			replaceContent(newFile);
		}//其他忽略
	}
	
	
	
	
	
	private static void replaceContent(File file) {
		StringBuilder sb = new StringBuilder();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = "";
			while((line = reader.readLine()) != null) {
				for (Entry<String, String> entry : dict.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if(line.contains(key)) {
						System.err.print("替换文件内容，原内容：" + line);
						line = line.replaceAll(key, value);
						System.err.println("新内容：" + line);
					}
				}
				sb.append(line+"\r\n");
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			writer.write(sb.toString(), 0, sb.length());
			writer.flush();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}


	private static File renameFile(File file) {
		return renameDir(file);
	}

	
	/**
	 * 重命名文件夹
	 * @return 
	 */
	private static File renameDir(File file) {
		String dirName = file.getName();
		File newFile = null;
		String parentAbsolutePath = file.getParentFile().getAbsolutePath();
		for (Entry<String, String> entry : dict.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if(dirName.contains(k)) {
				String newDirName = dirName.replaceAll(k, v);
				try {
					System.err.println("重命名文件或文件夹，原名："+ dirName + " , 新名："+newDirName);
					Path np = Paths.get(parentAbsolutePath, newDirName);
					Files.move(file.toPath(), np,StandardCopyOption.REPLACE_EXISTING);
					newFile = np.toFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return newFile==null?file:newFile;
	}


	/**
	 * 输入流
	 */
	public static String input() {
		String line = "";
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
	/**
	 * 默认文件过滤器
	 * @return
	 */
	public static FileFilter defaultFileFilter() {
		return defaultFileFilter;
	}
	
	

}
