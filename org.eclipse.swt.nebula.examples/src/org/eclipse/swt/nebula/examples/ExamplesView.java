/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    chris.gross@us.ibm.com - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.swt.nebula.examples;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Nebula examples view.
 * 
 * @author cgross
 */
public class ExamplesView extends ViewPart
{

    private TabFolder tabFolder;
    private static ImageRegistry imgRegistry = new ImageRegistry();
    
    
    public ExamplesView()
    {
        super();
    }

    public void createPartControl(Composite parent)
    {
        
        tabFolder = new TabFolder(parent,SWT.TOP);
        IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.swt.nebula.examples.examples");
        
        for (int i = 0; i < elements.length; i++) {
            IConfigurationElement element = elements[i];
            
            TabItem item = new TabItem(tabFolder,SWT.NONE);
            item.setText(element.getAttribute("name"));
            
            try {
                final AbstractExampleTab part = (AbstractExampleTab) element.createExecutableExtension("class");
                
                Composite client = new Composite(tabFolder,SWT.NONE);
                part.create(client);
                
                item.setControl(client);
                
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        
        tabFolder.setSelection(tabFolder.getItem(0));
        
    }

    public void setFocus()
    {
        tabFolder.setFocus();
    }
    
    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.nebula.examples", path);
    }
    
    /**
     * Returns an image for the image file at the given plug-in relative path.  This image is 
     * maintained in an ImageRegistry and will automatically be disposed.
     * 
     * @param path the path
     * @return the image
     */
    public static Image getImage(String path){
        Image i = imgRegistry.get(path);
        
        if (i == null){
            ImageDescriptor id = getImageDescriptor(path);
            if (id == null) return null;
            
            i = id.createImage();
            imgRegistry.put(path,i);
        }
        
        return i;        
    }

}
