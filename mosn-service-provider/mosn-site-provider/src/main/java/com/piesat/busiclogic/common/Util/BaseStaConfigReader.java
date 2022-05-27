package com.piesat.busiclogic.common.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.piesat.busiclogic.common.entity.AlgEntity;
import com.piesat.busiclogic.common.entity.ElementEntity;
import com.piesat.busiclogic.common.entity.TabEntity;

public class BaseStaConfigReader {

	public static List<TabEntity> getTabInfoByFilePath(String filePath) {
		
		List<TabEntity> list = new ArrayList<TabEntity>();
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(filePath));
			Element rootElement = document.getRootElement();
			Iterator<Element> elementIterator = rootElement.elementIterator();
			while (elementIterator.hasNext()) {
				Element tabElement = elementIterator.next();
				TabEntity tabEntity = new TabEntity();
				tabEntity.setId(tabElement.attributeValue("id"));
				tabEntity.setName(tabElement.attributeValue("name"));
				tabEntity.setTableName(tabElement.attributeValue("tableName"));
				List<AlgEntity> algInfoByTab = getAlgInfoByTab(tabElement);
				if(algInfoByTab != null && !algInfoByTab.isEmpty()) {
					tabEntity.setAlgs(algInfoByTab);
					list.add(tabEntity);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static List<AlgEntity> getAlgInfoByTab(Element tabElement) {
		
		List<AlgEntity> list = new ArrayList<AlgEntity>();
		Iterator<Element> algIterator = tabElement.elementIterator();
		while (algIterator.hasNext()) {
			Element algElement = algIterator.next();
			AlgEntity algEntity = new AlgEntity();
			algEntity.setId(algElement.attributeValue("id"));
			algEntity.setName(algElement.attributeValue("name"));
			algEntity.setDescription(algElement.attributeValue("description"));
			List<ElementEntity> elementInfoByalg = getElementInfoByalg(algElement);
			if(elementInfoByalg != null && !elementInfoByalg.isEmpty()) {
				algEntity.setElements(elementInfoByalg);
				list.add(algEntity);
			}
		}
		return list;
	}
	
	private static List<ElementEntity> getElementInfoByalg(Element algElement) {
		
		List<ElementEntity> list = new ArrayList<ElementEntity>();
		Iterator<Element> elementIterator = algElement.elementIterator();
		while (elementIterator.hasNext()) {
			Element element = elementIterator.next();
			ElementEntity elementEntity = new ElementEntity();
			elementEntity.setId(element.attributeValue("id"));
			elementEntity.setName(element.attributeValue("name"));
			elementEntity.setCols(element.attributeValue("cols"));
			list.add(elementEntity);
		}
		return list;
	}
	
	public static void main(String[] args) {
		List<TabEntity> tabInfoByFilePath = getTabInfoByFilePath("D:\\workSpace\\mdes\\mdescloud\\mdes-service-provider\\mdes-site-provider\\config\\baseAlgConfig.xml");
		List<TabEntity> tabInfoByFilePath2 = getTabInfoByFilePath("D:\\workSpace\\mdes\\mdescloud\\mdes-service-provider\\mdes-site-provider\\config\\baseAlgConfig.xml");
		System.out.println(tabInfoByFilePath.equals(tabInfoByFilePath2));
	}
}
