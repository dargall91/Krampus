/**
 * RouteMapPanel is the panel for accessing the RouteMap to see the visualization of
 * the Route Map.
 *
 * @autor alexeya, Kevin Dolan
 * @version 2.0
 */
package main.java.memoranda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Route;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.DuplicateKeyException;
import main.java.memoranda.util.Local;

public class RouteMapPanel extends JPanel {
    private final int BUTTON_HEIGHT = 30;
    private final int BUTTON_WIDTH = 150;

    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar toolBar = new JToolBar();
    private RouteMap map = new RouteMap();
    private JScrollPane scrollPane = new JScrollPane();
    private RouteTable routeTable = new RouteTable();
    private JScrollPane rScrollPane = new JScrollPane();

    private JButton newRouteB = new JButton();
    private JButton removeRouteB = new JButton();
    private JButton optRouteB = new JButton();
    private JButton optRouteWithStartB = new JButton();
    private JButton refreshB = new JButton();

    private JPopupMenu resPPMenu = new JPopupMenu();
    private JMenuItem ppRemoveRes = new JMenuItem();
    private JMenuItem ppNewRes = new JMenuItem();
    private JMenuItem ppRefresh = new JMenuItem();

    /**
     * Constructor for RouteMapPanel
     */
    public RouteMapPanel() {
        try {
            jbInit();
        }
        catch (Exception ex) {
           new ExceptionDialog(ex);
        }
    }

    /**
     * Initiates the JButtons on the panel.
     *
     * @throws Exception
     */
    void jbInit() throws Exception {
        toolBar.setFloatable(false);
        this.setLayout(borderLayout1);
        map.setMaximumSize(new Dimension(32767, 32767));

        newRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        newRouteB.setText("New");
        newRouteB.setEnabled(true);
        newRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setToolTipText(Local.getString("New route."));
        newRouteB.setRequestFocusEnabled(false);
        newRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        newRouteB.setFocusable(false);
        newRouteB.setBorderPainted(true);
        newRouteB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newRouteB_actionPerformed(e);
            }
        });

        removeRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        removeRouteB.setText("Remove");
        removeRouteB.setEnabled(true);
        removeRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setToolTipText(Local.getString("Remove route."));
        removeRouteB.setRequestFocusEnabled(false);
        removeRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        removeRouteB.setFocusable(false);
        removeRouteB.setBorderPainted(true);
        removeRouteB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //newResB_actionPerformed(e);
            }
        });


        optRouteB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        optRouteB.setText("Optimize");
        optRouteB.setEnabled(true);
        optRouteB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setToolTipText(Local.getString("Optimize route."));
        optRouteB.setRequestFocusEnabled(false);
        optRouteB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteB.setFocusable(false);
        optRouteB.setBorderPainted(true);
        optRouteB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //newResB_actionPerformed(e);
            }
        });


        optRouteWithStartB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        optRouteWithStartB.setText("Optimize w/Start");
        optRouteWithStartB.setEnabled(true);
        optRouteWithStartB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setToolTipText(Local.getString("Optimize route with best start."));
        optRouteWithStartB.setRequestFocusEnabled(false);
        optRouteWithStartB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        optRouteWithStartB.setFocusable(false);
        optRouteWithStartB.setBorderPainted(true);
        optRouteWithStartB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //newResB_actionPerformed(e);
            }
        });

        refreshB.setIcon(
                new ImageIcon(AppFrame.class.getResource("/ui/icons/addresource.png")));
        refreshB.setText("Refresh");
        refreshB.setEnabled(true);
        refreshB.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setToolTipText(Local.getString("Refresh map."));
        refreshB.setRequestFocusEnabled(false);
        refreshB.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        refreshB.setFocusable(false);
        refreshB.setBorderPainted(true);
        refreshB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //newResB_actionPerformed(e);
            }
        });

        routeTable.setMaximumSize(new Dimension(32767, 32767));
        routeTable.setRowHeight(24);
        rScrollPane.getViewport().setBackground(Color.lightGray);
        rScrollPane.getViewport().add(routeTable, null);

        toolBar.add(newRouteB, null);
        toolBar.addSeparator();
        toolBar.add(removeRouteB, null);
        toolBar.addSeparator();
        toolBar.add(optRouteB, null);
        toolBar.addSeparator();
        toolBar.add(optRouteWithStartB, null);
        toolBar.addSeparator();
        toolBar.add(refreshB, null);

        resPPMenu.addSeparator();
        resPPMenu.add(ppNewRes);
        resPPMenu.addSeparator();
        resPPMenu.add(ppRemoveRes);
        resPPMenu.addSeparator();
        resPPMenu.add(ppRefresh);

        scrollPane.getViewport().add(map, null);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(rScrollPane, BorderLayout.EAST);
        this.add(toolBar, BorderLayout.NORTH);




        //        newResB.setIcon(
//            new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/addresource.png")));
//        newResB.setEnabled(true);
//        newResB.setMaximumSize(new Dimension(24, 24));
//        newResB.setMinimumSize(new Dimension(24, 24));
//        newResB.setToolTipText(Local.getString("New resource"));
//        newResB.setRequestFocusEnabled(false);
//        newResB.setPreferredSize(new Dimension(24, 24));
//        newResB.setFocusable(false);
//        /*newResB.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                newResB_actionPerformed(e);
//            }
//        });*/
//        newResB.setBorderPainted(false);

        //map.setRowHeight(24);
//        removeResB.setBorderPainted(false);
//        removeResB.setFocusable(false);
//        /*removeResB.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                removeResB_actionPerformed(e);
//            }
//        });*/
//        removeResB.setPreferredSize(new Dimension(24, 24));
//        removeResB.setRequestFocusEnabled(false);
//        removeResB.setToolTipText(Local.getString("Remove resource"));
//        removeResB.setMinimumSize(new Dimension(24, 24));
//        removeResB.setMaximumSize(new Dimension(24, 24));
//        removeResB.setIcon(
//            new ImageIcon(
//                main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/removeresource.png")));
//        removeResB.setEnabled(false);
//        scrollPane.getViewport().setBackground(Color.white);
//        toolBar.addSeparator(new Dimension(8, 24));

        /*PopupListener ppListener = new PopupListener();
        scrollPane.addMouseListener(ppListener);
        map.addMouseListener(ppListener);*/

        /*map.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = (map.getRowCount() > 0) && (map.getSelectedRow() > -1);

                removeResB.setEnabled(enbl); ppRemoveRes.setEnabled(enbl);
                ppRun.setEnabled(enbl);
            }
        });*/
//        refreshB.setBorderPainted(false);
//        /*refreshB.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                refreshB_actionPerformed(e);
//            }
//        });*/
//        refreshB.setFocusable(false);
//        refreshB.setPreferredSize(new Dimension(24, 24));
//        refreshB.setRequestFocusEnabled(false);
//        refreshB.setToolTipText(Local.getString("Refresh"));
//        refreshB.setMinimumSize(new Dimension(24, 24));
//        refreshB.setMaximumSize(new Dimension(24, 24));
//        refreshB.setEnabled(true);
//        refreshB.setIcon(
//            new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/refreshres.png")));
//        resPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
//        ppRun.setFont(new java.awt.Font("Dialog", 1, 11));
//        ppRun.setText(Local.getString("Open resource")+"...");
//        /*ppRun.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ppRun_actionPerformed(e);
//            }
//        });*/
//        ppRun.setEnabled(false);
//
//        ppRemoveRes.setFont(new java.awt.Font("Dialog", 1, 11));
//        ppRemoveRes.setText(Local.getString("Remove resource"));
//        /*ppRemoveRes.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ppRemoveRes_actionPerformed(e);
//            }
//        });*/
//        ppRemoveRes.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/removeresource.png")));
//        ppRemoveRes.setEnabled(false);
//        ppNewRes.setFont(new java.awt.Font("Dialog", 1, 11));
//        ppNewRes.setText(Local.getString("New resource")+"...");
//        /*ppNewRes.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ppNewRes_actionPerformed(e);
//            }
//        });*/
//        ppNewRes.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/addresource.png")));
//
//        ppRefresh.setFont(new java.awt.Font("Dialog", 1, 11));
//        ppRefresh.setText(Local.getString("Refresh"));
//        /*ppRefresh.addActionListener(new java.awt.event.ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            ppRefresh_actionPerformed(e);
//        }
//        });*/
//        ppRefresh.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/refreshres.png")));

		// remove resources using the DEL key
       /*map.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e){
                if(map.getSelectedRows().length>0
                        && e.getKeyCode()==KeyEvent.VK_DELETE)
                    ppRemoveRes_actionPerformed(null);
            }
            public void	keyReleased(KeyEvent e){}
            public void keyTyped(KeyEvent e){}
        });	*/
    }

    public void newRouteB_actionPerformed(ActionEvent e) {
        RouteDialog dialog = new RouteDialog(App.getFrame(), "New Route");
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dialog.setLocation(
                (frmSize.width - dialog.getSize().width) / 2 + loc.x,
                (frmSize.height - dialog.getSize().height) / 2 + loc.y);
        dialog.setVisible(dialog.getError() != 1);

        if (!dialog.isComplete()) {
            Route route = CurrentProject.getRouteColl().newItem();
            route.setName(dialog.getName());

            try {
                CurrentProject.getRouteColl().add(route);
                CurrentStorage.get().storeRouteList(CurrentProject.get(), CurrentProject.getRouteColl());
                routeTable.refresh();
            } catch (DuplicateKeyException | IOException exception) {
                exception.printStackTrace();
            }
        }
    }

        /*void newResB_actionPerformed(ActionEvent e) {
        AddResourceDialog dlg = new AddResourceDialog(App.getFrame(), Local.getString("New resource"));
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        if (dlg.localFileRB.isSelected()) {
            String fpath = dlg.pathField.getText();
            MimeType mt = MimeTypesList.getMimeTypeForFile(fpath);
            if (mt.getMimeTypeId().equals("__UNKNOWN")) {
                mt = addResourceType(fpath);
                if (mt == null)
                    return;
            }
            if (!checkApp(mt))
                return;
            // if file if projectFile, than copy the file and change url.
            if (dlg.projectFileCB.isSelected()) {
            	fpath = copyFileToProjectDir(fpath);
            	CurrentProject.getResourcesList().addResource(fpath, false, true);
            }
            else
            	CurrentProject.getResourcesList().addResource(fpath);            	     	
            
            map.tableChanged();
        }
        else {
            if (!Util.checkBrowser())
                return;
            CurrentProject.getResourcesList().addResource(dlg.urlField.getText(), true, false);
            map.tableChanged();
        }
    }*/

    /*void removeResB_actionPerformed(ActionEvent e) {
        int[] toRemove = map.getSelectedRows();
        String msg = "";
        if (toRemove.length == 1)
            msg =
                Local.getString("Remove the shortcut to resource")
                    + "\n'"
                    + map.getModel().getValueAt(toRemove[0], 0)
                    + "'";

        else
            msg = Local.getString("Remove") + " " + toRemove.length + " " + Local.getString("shortcuts");
        msg +=
            "\n"
            + Local.getString("Are you sure?");
        int n =
            JOptionPane.showConfirmDialog(
                App.getFrame(),
                msg,
                Local.getString("Remove resource"),
                JOptionPane.YES_NO_OPTION);
        if (n != JOptionPane.YES_OPTION)
            return;
        for (int i = 0; i < toRemove.length; i++) {        	
        		CurrentProject.getResourcesList().removeResource(
                        ((Resource) map.getModel().getValueAt(toRemove[i], ResourcesTable._RESOURCE)).getPath());
        }
        map.tableChanged();
    }*/

    /*MimeType addResourceType(String fpath) {
        ResourceTypeDialog dlg = new ResourceTypeDialog(App.getFrame(), Local.getString("Resource type"));
        Dimension dlgSize = new Dimension(420, 300);
        dlg.setSize(dlgSize);
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.ext = MimeTypesList.getExtension(fpath);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return null;
        int ix = dlg.getTypesList().getSelectedIndex();
        MimeType mt = (MimeType) MimeTypesList.getAllMimeTypes().toArray()[ix];
        mt.addExtension(MimeTypesList.getExtension(fpath));
        CurrentStorage.get().storeMimeTypesList();
        return mt;
    }*/

    /*boolean checkApp(MimeType mt) {
        String appId = mt.getAppId();
        AppList appList = MimeTypesList.getAppList();
        File d;
        if (appId == null) {
            appId = Util.generateId();
            d = new File("/");
        }
        else {
            File exe = new File(appList.getFindPath(appId) + "/" + appList.getExec(appId));
            if (exe.isFile())
                return true;
            d = new File(exe.getParent());
            while (!d.exists())
                d = new File(d.getParent());
        }
        SetAppDialog dlg =
            new SetAppDialog(
                App.getFrame(),
                Local.getString(Local.getString("Select the application to open files of type")+" '" + mt.getLabel() + "'"));
        Dimension dlgSize = new Dimension(420, 300);
        dlg.setSize(dlgSize);
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setDirectory(d);
        dlg.appPanel.argumentsField.setText("$1");
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return false;
        File f = new File(dlg.appPanel.applicationField.getText());

        appList.addOrReplaceApp(
            appId,
            f.getParent().replace('\\', '/'),
            f.getName().replace('\\', '/'),
            dlg.appPanel.argumentsField.getText());
        mt.setApp(appId);
        /*appList.setFindPath(appId, chooser.getSelectedFile().getParent().replace('\\','/'));
        appList.setExec(appId, chooser.getSelectedFile().getName().replace('\\','/'));
        CurrentStorage.get().storeMimeTypesList();
        return true;
    }*/
    

    /*void runApp(String fpath) {
        MimeType mt = MimeTypesList.getMimeTypeForFile(fpath);
        if (mt.getMimeTypeId().equals("__UNKNOWN")) {
            mt = addResourceType(fpath);
            if (mt == null)
                return;
        }
        if (!checkApp(mt))
            return;
        String[] command = MimeTypesList.getAppList().getCommand(mt.getAppId(), fpath);
        if (command == null)
            return;
        //DEBUG
        System.out.println("Run: " + command[0]);
        try {
            Runtime.getRuntime().exec(command);
        }
        catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to run an external application <br><code>"
                    +command[0]+"</code>", "Check the application path and command line parameters for this resource type " +
                    		"(File-&gt;Preferences-&gt;Resource types).");
        }
    }

    void runBrowser(String url) {
        Util.runBrowser(url);
    }*/

    /*class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (map.getSelectedRow() > -1)) {
                String path = (String) map.getValueAt(map.getSelectedRow(), 3);
                if (path.length() >0)
                    runApp(path);
                else
                    runBrowser((String) map.getValueAt(map.getSelectedRow(), 0));
            }
            //editTaskB_actionPerformed(null);
        }

                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }

                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        resPPMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }

    }
    void refreshB_actionPerformed(ActionEvent e) {
        map.tableChanged();
    }*/

  /*void ppRun_actionPerformed(ActionEvent e) {
    String path = (String) map.getValueAt(map.getSelectedRow(), 3);
                if (path.length() >0)
                    runApp(path);
                else
                    runBrowser((String) map.getValueAt(map.getSelectedRow(), 0));
  }
  void ppRemoveRes_actionPerformed(ActionEvent e) {
    removeResB_actionPerformed(e);
  }
  void ppNewRes_actionPerformed(ActionEvent e) {
    newResB_actionPerformed(e);
  }

  void ppRefresh_actionPerformed(ActionEvent e) {
     map.tableChanged();
  }*/
//
//  /**
//   * Copy a file to the directory of the current project
//   * @param srcStr The path of the source file.
//   * @param destStr The destination path.
//   * @return The new path of the file.
//   */
  /*String copyFileToProjectDir(String srcStr) {
	  
	  String JN_DOCPATH = Util.getEnvDir();	    
	  
	  String baseName;
	  int i = srcStr.lastIndexOf( File.separator );
		if ( i != -1 ) {
			baseName = srcStr.substring(i+1);
		} else
			baseName = srcStr;
		
	  String destStr = JN_DOCPATH + CurrentProject.get().getID() 
	  				   + File.separator + "_projectFiles" + File.separator + baseName;
	  
	  File f = new File(JN_DOCPATH + CurrentProject.get().getID() + File.separator + "_projectFiles");
	  if (!f.exists()) {
		  f.mkdirs();
	  }	  
	  System.out.println("[DEBUG] Copy file from: "+srcStr+" to: "+destStr);
	  
	  try {
         FileInputStream in = new FileInputStream(srcStr);
         FileOutputStream out = new FileOutputStream(destStr);
         byte[] buf = new byte[4096];
         int len;
         while ((len = in.read(buf)) > 0) {
           out.write(buf, 0, len);
         }
         out.close();
         in.close();
       } 
	   catch (IOException e) {
         System.err.println(e.toString());
       }
		     
  return destStr;
  }*/
}