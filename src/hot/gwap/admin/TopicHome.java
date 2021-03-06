/*
 * This file is part of gwap, an open platform for games with a purpose
 *
 * Copyright (C) 2013
 * Project play4science
 * Lehr- und Forschungseinheit für Programmier- und Modellierungssprachen
 * Ludwig-Maximilians-Universität München
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gwap.admin;

import gwap.model.Topic;
import gwap.model.resource.Resource;
import gwap.model.resource.Term;
import gwap.tools.CustomSourceBean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;

import com.google.common.base.Strings;

/**
 * @author Mislav Boras
 */
@Name("topicHome")
public class TopicHome extends EntityHome<Topic> {

	private static final long serialVersionUID = 8849345213839186469L;

	@RequestParameter
	Long topicId;
	@In
	private FacesMessages facesMessages;
	@In
	private EntityManager entityManager;
	@In
	private LocaleSelector localeSelector;
	@Logger
	private Log log;
	private List<Resource> allResources = null;
	private List<Resource> selectedResources = null;
	private List<String> list = new ArrayList<String>();

	@In
	private CustomSourceBean customSourceBean;


	public List<Resource> getSelectedResources() {
		return selectedResources;
	}

	public void setSelectedResources(List<Resource> selectedResources) {
		this.selectedResources = selectedResources;
	}


	public List<Term> getList() {
		List<Term> termList = new ArrayList<Term>();
		selectedResources = getInstance().getResources();

		if (selectedResources != null) {

			for (Resource r : selectedResources) {
				Term t = (Term) r;
			    termList.add(t);
			}
		}

		return termList;
	}

	public void setList(List<String> rightSideValues) {
		this.list = rightSideValues;
	}

	@Override
	@Begin(join = true)
	public void create() {
		super.create();
		getInstance().setSource(customSourceBean.getCustomSource());
	}

	@Override
	public Object getId() {
		if (topicId == null)
			return super.getId();
		else
			return topicId;
	}
	
	public void setAllResources(List<Resource> allResources) {
		this.allResources = allResources;
	}
	
	@Override
	public String persist() {
		if (Strings.isNullOrEmpty(getInstance().getName())) {
			facesMessages.addToControlFromResourceBundle("topicName", "javax.faces.component.UIInput.REQUIRED");
			return null;
		}
		String persist = super.persist();
		addResources();
		return persist;
	};

	@Override
	public String update() {
		String update = super.update();
		addResources();
		return update;
	};
	
	
	public List<Resource> getAllResources() {  
		if (allResources == null) {
			Query q = customSourceBean.query("term.allTerms");
			List<Term> allTerms = q.getResultList();
			allResources = new ArrayList<Resource>();
			for (Term term : allTerms) {
				allResources.add(term);
			}
		}
		return allResources;
	}

	public void addResources() {
		selectedResources = null;
		selectedResources = new ArrayList<Resource>();
		if (list != null) {
			for (Resource r : allResources) {
				for (String id : list) {
					if (r.toString().equals(id)) {
						selectedResources.add(r);
					}
				}
			}

//			for (String s : list) {
//				 log.info(s);
//			}
		}
		
		getInstance().setResources(selectedResources);
	}
}
