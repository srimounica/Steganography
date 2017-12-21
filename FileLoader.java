
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.filechooser.*;
import java.beans.*;

class MyFileFilter extends FileFilter {

	private static String HIDDEN_FILE = "Hidden File";
	private Hashtable filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	public MyFileFilter() {
		this.filters = new Hashtable();
	}

	public MyFileFilter(String extension) {
		this(extension, null);
	}

	public MyFileFilter(String extension, String description) {
		this();
		if (extension != null)
			addExtension(extension);
		if (description != null)
			setDescription(description);
	}

	public MyFileFilter(String[] filters) {
		this(filters, null);
	}

	public MyFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
			addExtension(filters[i]);
		}
		if (description != null)
			setDescription(description);
	}

	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) {
				return true;
			}
			;
		}
		return false;
	}

	// Return the extension portion of the file's name .
	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}

	public void addExtension(String extension) {
		if (filters == null) {
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

	public String getDescription() {
		if (fullDescription == null) {
			if (description == null || isExtensionListInDescription()) {
				fullDescription = description == null ? "(" : description + " (";
				// build the description from the extension list
				Enumeration extensions = filters.keys();
				if (extensions != null) {
					fullDescription += "." + (String) extensions.nextElement();
					while (extensions.hasMoreElements()) {
						fullDescription += ", ." + (String) extensions.nextElement();
					}
				}
				fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}
}


class MyFileView extends FileView {
	private Hashtable icons = new Hashtable(5);
	private Hashtable fileDescriptions = new Hashtable(5);
	private Hashtable typeDescriptions = new Hashtable(5);

	public String getName(File f) {
		return null;
	}

	public void putDescription(File f, String fileDescription) {
		fileDescriptions.put(fileDescription, f);
	}

	public String getDescription(File f) {
		return (String) fileDescriptions.get(f);
	};

	public void putTypeDescription(String extension, String typeDescription) {
		typeDescriptions.put(typeDescription, extension);
	}

	public void putTypeDescription(File f, String typeDescription) {
		putTypeDescription(getExtension(f), typeDescription);
	}

	public String getTypeDescription(File f) {
		return (String) typeDescriptions.get(getExtension(f));
	}

	public String getExtension(File f) {
		String name = f.getName();
		if (name != null) {
			int extensionIndex = name.lastIndexOf('.');
			if (extensionIndex < 0) {
				return null;
			}
			return name.substring(extensionIndex + 1).toLowerCase();
		}
		return null;
	}

	public void putIcon(String extension, Icon icon) {
		icons.put(extension, icon);
	}

	public Icon getIcon(File f) {
		Icon icon = null;
		String extension = getExtension(f);
		if (extension != null) {
			icon = (Icon) icons.get(extension);
		}
		return icon;
	}

	public Boolean isHidden(File f) {
		String name = f.getName();
		if (name != null && !name.equals("") && name.charAt(0) == '.') {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	};
}
