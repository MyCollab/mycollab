/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.vaadin.easyuploads;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.common.events.JettyListenerEventDriver;

import com.esofthead.mycollab.vaadin.ui.AttachmentUploadComponent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamVariable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MultiFileUploadExt extends CssLayout implements DropHandler {
	private static final long serialVersionUID = 1L;
	private JettyListenerEventDriver a;
	private AttachmentUploadComponent attachmentDisplayComponent;
	private VerticalLayout progressBars = new VerticalLayout();
	private CssLayout uploads = new CssLayout();
	private String uploadButtonCaption = "Attach File(s)";
	private MultiUpload upload;
	private int oldPollInterval;

	public MultiFileUploadExt(
			AttachmentUploadComponent attachmentDisplayComponent) {
		this.attachmentDisplayComponent = attachmentDisplayComponent;
		this.attachmentDisplayComponent.registerMultiUpload(this);
		progressBars.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		setStyleName("v-multifileupload-ext");
		addComponent(progressBars);
		uploads.setStyleName("v-multifileupload-uploads");
		addComponent(uploads);
		prepareUpload();
		oldPollInterval = UI.getCurrent().getPollInterval();
	}

	public void removeAndReInitMultiUpload() {
		uploads.removeComponent(upload);
		upload = null;
		prepareUpload();
	}

	private void prepareUpload() {
		final FileBuffer receiver = createReceiver();

		upload = new MultiUpload();
		MultiUploadHandlerExt handler = new MultiUploadHandlerExt() {
			private static final long serialVersionUID = 1L;
			private LinkedList<ProgressBar> indicators;

			@Override
			public void streamingStarted(
					StreamVariable.StreamingStartEvent event) {
				UI.getCurrent().setPollInterval(1000);
			}

			@Override
			public void streamingFinished(StreamVariable.StreamingEndEvent event) {
				if (!indicators.isEmpty()) {
					progressBars.removeComponent(indicators.remove(0));
				}
				File file = receiver.getFile();

				handleFile(file, event.getFileName(), event.getMimeType(),
						event.getBytesReceived());
				receiver.setValue(null);
				UI.getCurrent().setPollInterval(oldPollInterval);
			}

			@Override
			public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
				Logger.getLogger(getClass().getName()).log(Level.FINE,
						"Streaming failed", event.getException());

				for (ProgressBar progressIndicator : indicators) {
					progressBars.removeComponent(progressIndicator);
				}
				UI.getCurrent().setPollInterval(oldPollInterval);
			}

			@Override
			public void onProgress(StreamVariable.StreamingProgressEvent event) {
				long readBytes = event.getBytesReceived();
				long contentLength = event.getContentLength();
				float f = (float) readBytes / (float) contentLength;
				indicators.get(0).setValue(f);
			}

			@Override
			public OutputStream getOutputStream() {
				MultiUpload.FileDetail next = upload.getPendingFileNames()
						.iterator().next();
				return receiver.receiveUpload(next.getFileName(),
						next.getMimeType());
			}

			@Override
			public void filesQueued(
					Collection<MultiUpload.FileDetail> pendingFileNames) {
				if (indicators == null) {
					indicators = new LinkedList<ProgressBar>();
				}
				for (MultiUpload.FileDetail f : pendingFileNames) {
					ProgressBar pi = createProgressIndicator();
					progressBars.addComponent(pi);
					pi.setCaption(f.getFileName());
					pi.setVisible(true);
					indicators.add(pi);
				}
			}

			@Override
			public boolean isInterrupted() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		upload.setHandler(handler);
		upload.setButtonCaption(getUploadButtonCaption());
		uploads.addComponent(upload);

	}

	private ProgressBar createProgressIndicator() {
		ProgressBar progressIndicator = new ProgressBar();
		progressIndicator.setValue(0f);
		return progressIndicator;
	}

	public String getUploadButtonCaption() {
		return uploadButtonCaption;
	}

	public void setUploadButtonCaption(String uploadButtonCaption) {
		this.uploadButtonCaption = uploadButtonCaption;
		Iterator<Component> componentIterator = uploads.iterator();
		while (componentIterator.hasNext()) {
			Component next = componentIterator.next();
			if (next instanceof MultiUpload) {
				MultiUpload upload = (MultiUpload) next;
				if (upload.isVisible()) {
					upload.setButtonCaption(getUploadButtonCaption());
				}
			}
		}
	}

	private FileFactory fileFactory;

	public FileFactory getFileFactory() {
		if (fileFactory == null) {
			fileFactory = new TempFileFactory();
		}
		return fileFactory;
	}

	public void setFileFactory(FileFactory fileFactory) {
		this.fileFactory = fileFactory;
	}

	protected FileBuffer createReceiver() {
		FileBuffer receiver = new FileBuffer(UploadField.FieldType.FILE) {
			private static final long serialVersionUID = 1L;

			@Override
			public FileFactory getFileFactory() {
				return MultiFileUploadExt.this.getFileFactory();
			}
		};
		receiver.setDeleteFiles(false);
		return receiver;
	}

	protected int getPollinInterval() {
		return 300;
	}

	@Override
	public void attach() {
		super.attach();
		if (supportsFileDrops()) {
			prepareDropZone();
		}
	}

	private DragAndDropWrapper dropZone;

	/**
	 * Sets up DragAndDropWrapper to accept multi file drops.
	 */
	private void prepareDropZone() {
		if (dropZone == null) {
			Component label = new Label(getAreaText(), ContentMode.HTML);
			label.setSizeUndefined();
			dropZone = new DragAndDropWrapper(label);
			dropZone.setStyleName("v-multifileupload-dropzone");
			dropZone.setSizeUndefined();
			addComponent(dropZone, 1);
			dropZone.setDropHandler(this);
			addStyleName("no-horizontal-drag-hints");
			addStyleName("no-vertical-drag-hints");
		}
	}

	protected String getAreaText() {
		return "<small>DROP<br/>FILES</small>";
	}

	protected boolean supportsFileDrops() {
		// AbstractWebApplicationContext context =
		// (AbstractWebApplicationContext) AppContext.getApplication()
		// .getContext();
		// WebBrowser browser = context.getBrowser();
		// if (browser.isChrome()) {
		// return true;
		// } else if (browser.isFirefox()) {
		// return true;
		// } else if (browser.isSafari()) {
		// return true;
		// }
		return false;
	}

	protected void handleFile(File file, String fileName, String mimeType,
			long length) {
		attachmentDisplayComponent
				.receiveFile(file, fileName, mimeType, length);
	}

	/**
	 * A helper method to set DirectoryFileFactory with given pathname as
	 * directory.
	 * 
	 * @param file
	 */
	public void setRootDirectory(String directoryWhereToUpload) {
		setFileFactory(new DirectoryFileFactory(
				new File(directoryWhereToUpload)));
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		// accept only files
		// return new And(new TargetDetailIs("verticalLocation","MIDDLE"), new
		// TargetDetailIs("horizontalLoction", "MIDDLE"));
		return AcceptAll.get();
	}

	@Override
	public void drop(DragAndDropEvent event) {
		DragAndDropWrapper.WrapperTransferable transferable = (DragAndDropWrapper.WrapperTransferable) event
				.getTransferable();
		Html5File[] files = transferable.getFiles();
		for (final Html5File html5File : files) {
			final ProgressBar pi = new ProgressBar();
			pi.setCaption(html5File.getFileName());
			progressBars.addComponent(pi);
			final FileBuffer receiver = createReceiver();
			html5File.setStreamVariable(new StreamVariable() {
				private static final long serialVersionUID = 1L;
				private String name;
				private String mime;

				@Override
				public OutputStream getOutputStream() {
					return receiver.receiveUpload(name, mime);
				}

				@Override
				public boolean listenProgress() {
					return true;
				}

				@Override
				public void onProgress(
						StreamVariable.StreamingProgressEvent event) {
					float p = (float) event.getBytesReceived()
							/ (float) event.getContentLength();
					pi.setValue(p);
				}

				@Override
				public void streamingStarted(
						StreamVariable.StreamingStartEvent event) {
					name = event.getFileName();
					mime = event.getMimeType();
					UI.getCurrent().setPollInterval(300);
				}

				@Override
				public void streamingFinished(
						StreamVariable.StreamingEndEvent event) {
					progressBars.removeComponent(pi);
					handleFile(receiver.getFile(), html5File.getFileName(),
							html5File.getType(), html5File.getFileSize());
					receiver.setValue(null);
					UI.getCurrent().setPollInterval(oldPollInterval);
				}

				@Override
				public void streamingFailed(
						StreamVariable.StreamingErrorEvent event) {
					progressBars.removeComponent(pi);
					UI.getCurrent().setPollInterval(oldPollInterval);
				}

				@Override
				public boolean isInterrupted() {
					return false;
				}
			});
		}

	}

	private static interface MultiUploadHandlerExt extends MultiUploadHandler,
			Serializable {

	}
}
