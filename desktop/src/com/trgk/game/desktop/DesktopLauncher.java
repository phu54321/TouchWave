package com.trgk.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.trgk.game.TouchWave;

import java.io.*;
import java.util.*;

public class DesktopLauncher {
    static long mix(long a, long b) {
        for (int i = 0; i < 40; i++) {
            long b2 = b;
            b = a ^ ((b >> 1) * 7 + 0xBBBBBBBB);
            a = b2;
        }
        return a ^ b;
    }

    private static long hashModDate(String dirPath) {
        File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        Arrays.sort(fileList,               // Need in reproducible order
                new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                });

        long hash = 0x123456789ABCDEF1L;

        for (File f : fileList) {
            if(!f.getName().startsWith(".")) {
                if(f.getName().compareTo("dirhash.dat") == 0) continue;
                long modTime = f.lastModified();
                hash = mix(hash, f.getName().hashCode());
                hash = mix(hash, modTime);
            }
        }

        return hash;
    }

    public static boolean dirChanged() {
        String oldHash = "";
        try {
            BufferedReader fin_buf = new BufferedReader(
                    new FileReader("../../Graphics/dirhash.dat")
            );
            oldHash = fin_buf.readLine();
            fin_buf.close();
        }
        catch(FileNotFoundException e) {}
        catch(IOException e) {}

        String newHash = Long.toString(hashModDate("../../Graphics/img"));
        if(newHash.compareTo(oldHash) != 0) {
            try {
                BufferedWriter fout = new BufferedWriter(
                        new FileWriter("../../Graphics/dirhash.dat")
                );
                fout.write(newHash);
                fout.close();
            }
            catch(IOException e) {}
            return true;
        }
        else return false;
    }

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 2;
		config.width = 960;
		config.height = 640;

        if(dirChanged()) {  // Set to true if graphics had changed.
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.duplicatePadding = false;
            settings.edgePadding = false;
            settings.fast = true;
            settings.filterMin = Texture.TextureFilter.MipMapLinearLinear;
            settings.filterMag = Texture.TextureFilter.Linear;
            TexturePacker.process(settings, "../../Graphics/img", "img", "dptextures");
        }

		new LwjglApplication(new TouchWave(), config);
	}
}
