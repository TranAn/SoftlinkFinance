/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.softlink.financeuser.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.softlink.financeuser.client.view.TopPanel;
import com.softlink.financeuser.client.request.UserServicesRequest;
import com.softlink.financeuser.client.request.UserServicesRequestAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UserService implements EntryPoint {

	private final static UserServicesRequestAsync userservices = 
			  GWT.create(UserServicesRequest.class);
	
	public void onModuleLoad() {
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("100%", "100%");
		
		final DockPanel dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 10, 0);
		dockPanel.setSize("99%", "100%");
		
		final TopPanel topPanel = new TopPanel();
		dockPanel.add(topPanel, DockPanel.CENTER);
		topPanel.setWidth("1340px");
		topPanel.setHeight("580px");
		
		userservices.isUserLoggedIn(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				if(result==true){		
					topPanel.setLogoutState();
				}
				if(result==false){
					Window.Location.replace("/");
				}
			}
	    });
	}
}
