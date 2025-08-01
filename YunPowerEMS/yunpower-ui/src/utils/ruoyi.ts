import { CascaderOption, TreeNodeData } from "@arco-design/web-vue";
import Color from "color";
/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id   id 字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(
  data: any,
  id: any = "id",
  parentId: any = "parentId",
  children: any = "children"
) {
  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children",
  };

  var childrenListMap: any = {};
  var nodeIds: any = {};
  var tree: any = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o: any) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 构造树型结构数据-下拉树
 * @param {*} data 数据源
 * @param [key="key"] 主键字段
 * @param [title="title"] 展示名称字段
 * @param [parentId="parentId"] 父类ID字段
 */
export function handleTreeNodeData(
  data: any[] | undefined,
  key: any = "key",
  title: any = "title",
  parentId: any = "parentId"
): TreeNodeData[] {
  let config = {
    key: key || "key",
    title: title || "title",
    parentId: parentId || "parentId",
  };
  if (!data || data.length <= 0) {
    return new Array();
  }
  let treeData: TreeNodeData[] = getChildList(data, 0);

  treeData.forEach((item) => {
    recursionFn(data, item);
  });

  function recursionFn(data: any[], item: TreeNodeData) {
    item.children = getChildList(data, item.key);
    if (item.children && item.children.length > 0) {
      item.children.forEach((citem) => {
        if (hasChild(data, citem.key)) {
          recursionFn(data, citem);
        } else {
          citem.children = undefined;
        }
      });
    } else {
      item.children = undefined;
    }
  }

  function hasChild(data: any[], parentId?: string | number) {
    return getChildList(data, parentId) ? true : false;
  }

  function getChildList(
    data: any[],
    parentId?: string | number
  ): TreeNodeData[] {
    let childrenData: TreeNodeData[] = new Array();
    data.forEach((item) => {
      if (item[config.parentId] === parentId) {
        childrenData.push({
          key: item[config.key],
          title: item[config.title],
        });
      }
    });
    return childrenData;
  }
  return treeData;
}

/**
 * 构造树型结构数据-级联选择
 * @param {*} data 数据源
 * @param [value="value"] 主键字段
 * @param [label="label"] 展示名称字段
 * @param [parentId="parentId"] 父类ID字段
 * @returns 级联选择 Cascader
 */
export function handleCascaderOptionData(
  data: any[] | undefined,
  value: any = "value",
  label: any = "label",
  parentId: any = "parentId"
): CascaderOption[] {
  let config = {
    value: value || "value",
    label: label || "title",
    parentId: parentId || "parentId",
  };

  if (!data || data.length <= 0) {
    return new Array();
  }
  let treeData: CascaderOption[] = getChildList(data, 0);

  treeData.forEach((item) => {
    recursionFn(data, item);
  });

  function recursionFn(data: any[], item: CascaderOption) {
    item.children = getChildList(data, item.value);
    if (item.children && item.children.length > 0) {
      item.children.forEach((citem) => {
        if (hasChild(data, citem.value)) {
          recursionFn(data, citem);
        } else {
          citem.children = undefined;
        }
      });
    } else {
      item.children = undefined;
    }
  }

  function hasChild(
    data: any[],
    parentId?: string | number | Record<string, any>
  ) {
    return getChildList(data, parentId) ? true : false;
  }

  function getChildList(
    data: any[],
    parentId?: string | number | Record<string, any>
  ): CascaderOption[] {
    let childrenData: CascaderOption[] = new Array();
    data.forEach((item) => {
      if (item[config.parentId] === parentId) {
        childrenData.push({
          value: item[config.value],
          label: item[config.label],
        });
      }
    });
    return childrenData;
  }
  return treeData;
}

// 添加日期范围
export function addDateRange(params: any, dateRange: any, propName: any) {
  let search = params;
  search.params =
    typeof search.params === "object" &&
    search.params !== null &&
    !Array.isArray(search.params)
      ? search.params
      : {};
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof propName === "undefined") {
    search.params["beginTime"] = dateRange[0];
    search.params["endTime"] = dateRange[1];
  } else {
    search.params["begin" + propName] = dateRange[0];
    search.params["end" + propName] = dateRange[1];
  }
  return search;
}

/**
 * 下拉设备树- 设置 Selectable
 * @param data
 */
export function processSelectable(data: any, isGroup: boolean = false) {
  data.forEach((item: any, index: number) => {
    item.selectable = !item.isGroup;
    if (isGroup) {
      item.key = `${item.id}@${item.deviceSn}`;
    } else {
      item.key = item.id;
    }
    if (item.children) {
      processSelectable(item.children, isGroup);
    }
  });
}

/**
 * 递归查询数据
 * @param data
 * @param targetId
 * @returns
 */
export function findById(data: any, targetId: any): any {
  for (const item of data) {
    if (item.id === targetId) {
      return item;
    }
    if (Array.isArray(item.children)) {
      const result = findById(item.children, targetId);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

/**
 * 下拉站点- 设置 Selectable
 * @param data
 */
export function processSelectableByCompany(data: any) {
  data.forEach((item: any, index: number) => {
    if (item.deptName == "0") {
      item.deptName = "请选择站点";
    }
    item.selectable = item.isCanSelect == 1 ? true : false;
    if (item.children) {
      processSelectableByCompany(item.children);
    }
  });
}

/**
 * 移除
 * @param data
 */
export function handleRemoveChildren(treeData: any) {
  treeData.forEach((item: any) => {
    if (item.children && item.children.length > 0) {
      handleRemoveChildren(item.children);
    } else {
      item.children = undefined;
    }
  });
}

/**
 * 获取第一个
 * @param data
 * @returns
 */
export function findFirstItem(data: any[]): any | null {
  for (const item of data) {
    if (!item.children) {
      return item;
    } else {
      const result = findFirstItem(item.children);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

/**
 * 获取第一个路由地址
 * @param data
 * @returns
 */
export function findFirstItemPath(data: any[], path: string): any | null {
  for (const item of data) {
    if (!item.children) {
      path += `/${item.path}`;
      return path;
    } else {
      path += `/${item.path}`;
      const result = findFirstItemPath(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return null;
}



/**
* 参数处理
* @param {*} params  参数
*/
export function tansParams(params:any) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && value !== "" && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}



// 验证是否为blob格式
export async function blobValidate(data:any) {
  try {
    const text = await data.text();
    JSON.parse(text);
    return false;
  } catch (error) {
    return true;
  }
}

/**
 * 生成相似颜色 降低饱和度 降低亮度
 * @param backgroundColor 指定颜色
 * @returns
 */
export  function generateSimilarColor(backgroundColor: string) {
  let rgba: string = backgroundColor || '';
  try {
    // 解析 RGBA 颜色
    const color = Color(rgba);
    // 降低饱和度
    let desaturatedColor = color.desaturate(0.2).alpha(color.alpha());
     // 降低亮度
     desaturatedColor =  color.darken(0.2).alpha(color.alpha());
    return desaturatedColor.rgb().string();
  } catch (error) {
    console.error('Error parsing color:', error);
    return rgba; // 解析失败时返回原始值
  }
}
