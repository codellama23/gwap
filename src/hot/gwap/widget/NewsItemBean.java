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

package gwap.widget;

import gwap.model.NewsItem;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.LocaleSelector;

/**
 * @author Fabian Kneißl
 */
@Name("newsItemBean")
@Scope(ScopeType.PAGE)
public class NewsItemBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@In String platform;
	@In LocaleSelector localeSelector;
	@In EntityManager entityManager;
	
	public List<NewsItem> currentNewsItems() {
		Query query = entityManager.createNamedQuery("newsItem.current");
		query.setParameter("platform", platform);
		query.setParameter("language", localeSelector.getLanguage());
		return query.getResultList();
	}

}
