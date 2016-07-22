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

package com.trgk.touchwave;

import com.trgk.touchwave.utils.MessageBox;
import com.trgk.touchwave.utils.MessageBoxToken;

import org.robovm.apple.uikit.UIAlertView;

public class MessageBoxIOS extends MessageBox{
    @Override
    public MessageBoxToken alertImpl(final String title, final String message) {
        UIAlertView newAlertView = new UIAlertView(title, message, null, "OK");
        newAlertView.show();
        newAlertView.release();
        newAlertView.dispose();

        MessageBoxToken token = new MessageBoxToken();
        token.completed = true;
        return token;
    }
}
