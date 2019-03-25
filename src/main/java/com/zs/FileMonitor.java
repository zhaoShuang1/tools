package com.zs;

import java.io.File;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileMonitor implements FileAlterationListener{

	public void onStart(FileAlterationObserver observer) {
		//System.out.println("onStart");
	}

	public void onDirectoryCreate(File directory) {
		System.out.println("onDirectoryCreate");
	}

	public void onDirectoryChange(File directory) {
		System.out.println("onDirectoryChange");
	}

	public void onDirectoryDelete(File directory) {
		System.out.println("onDirectoryDelete");
	}

	public void onFileCreate(File file) {
		System.out.println("onFileCreate");
	}

	public void onFileChange(File file) {
		System.out.println("onFileChange");
	}

	public void onFileDelete(File file) {
		System.out.println("onFileDelete");
	}

	public void onStop(FileAlterationObserver observer) {
		//System.out.println("onStop");
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		FileAlterationMonitor monitor = new FileAlterationMonitor(1000);
		//monitor directory
		IOFileFilter directorys = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(),HiddenFileFilter.VISIBLE);
		
		//监视文件
		IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(),FileFilterUtils.suffixFileFilter(".java"));
		
		IOFileFilter fileFilter = FileFilterUtils.or(directorys,files);
		FileAlterationObserver observer = new FileAlterationObserver("D:\\myWork\\Demo", fileFilter);
		
		observer.addListener(new FileMonitor());
		monitor.addObserver(observer );
		monitor.start();
		
	}
	
	

}
