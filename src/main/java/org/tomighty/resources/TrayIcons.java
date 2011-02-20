/*
 * Copyright (c) 2010 Célio Cidral Junior
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.tomighty.resources;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;

import org.tomighty.ioc.Inject;
import org.tomighty.resources.cache.Cache;
import org.tomighty.resources.cache.Caches;
import org.tomighty.resources.cache.Icons;
import org.tomighty.time.Time;
import org.tomighty.ui.state.laf.theme.ColorTone;
import org.tomighty.ui.state.laf.theme.Theme;
import org.tomighty.ui.util.Canvas;

public class TrayIcons {

	@Inject private Resources resources;
	@Inject private Theme theme;
	@Inject private Caches caches;
	
	public Image tomato() {
		int size = traySize().height;
		Image image = tomato(size);
		if(image == null) {
			image = tomato(16);
		}
		return image;
	}

	public Image time(Time time) {
		String iconName = iconNameFor(time);
		Cache cache = caches.of(Icons.class);
		if(cache.contains(iconName)) {
			return cache.get(iconName);
		}
		
		Dimension size = traySize();
		ColorTone colors = theme.colorTone();
		Canvas canvas = new Canvas(size);
		canvas.fontSize((float)size.height * 0.58f);
		canvas.drawGradientBackground(colors);
		canvas.drawBorder(colors.light().darker());
		canvas.drawCentralizedText(time.shortestString());
		
		cache.store(canvas.image(), iconName);
		
		return canvas.image();
	}
	
	private String iconNameFor(Time time) {
		Font font = Canvas.defaultFont();
		Dimension size = traySize();
		String colorName = theme.colorTone().getClass().getSimpleName();
		return font.getFontName() + "_" + size.width + "x" + size.height + "_" + colorName + "_" + time.shortestString();
	}

	private Image tomato(int size) {
		return resources.image("/tomato-"+size+".png");
	}

	private Dimension traySize() {
		return SystemTray.getSystemTray().getTrayIconSize();
	}

}
