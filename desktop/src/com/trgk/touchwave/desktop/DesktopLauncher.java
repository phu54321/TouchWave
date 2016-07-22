/*
 * Copyright 2015 Hyun Woo Park
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this
 * exception to your version of the library, but you are not obliged to
 * do so. If you do not wish to do so, delete this exception statement
 * from your version.
 */

package com.trgk.touchwave.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.trgk.touchwave.TouchWave;
import com.trgk.touchwave.utils.MessageBox;

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

        MessageBox.setImpl(new MessageBoxDesktop());
		new LwjglApplication(new TouchWave(), config);
	}
}
