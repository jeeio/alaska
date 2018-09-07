package io.jee.alaska.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import io.jee.alaska.bean.BaseTree;

public class TreeUtil {
	
	/**
	 * 普通结构转换成树形结构
	 * <p>必需包含id, panrentId, children 属性
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-07-24 16:38:59
	 *
	 * @param nodes
	 * @param parentId
	 * @return
	 */
	public static <T extends BaseTree<T>> List<T> transferTreeStruct(List<T> nodes, Integer parentId) {
		List<T> listParent = new ArrayList<>();
		List<T> listNotParent = new ArrayList<>();
		// 遍历nodes，找出所有的根节点和非根节点
		if (!CollectionUtils.isEmpty(nodes)) {
			for (T node : nodes) {
				if (node.getParentId() == parentId) {
					listParent.add(node);
				} else {
					listNotParent.add(node);
				}

			}
		}
		// 第二步： 递归获取所有子节点
		if (!CollectionUtils.isEmpty(listParent)) {
			for (T parent : listParent) {
				parent.setChildren(transferTreeStruct(listNotParent, parent.getId()));
			}
		}
		return listParent;
	}
}
