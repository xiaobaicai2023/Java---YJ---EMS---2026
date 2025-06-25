import useDictStore from "@/store/modules/dict/";
import { getDict } from "@/api/public";
import { ref, toRefs } from "vue";

/**
 * 获取字典数据
 */
export function useDict(...args: any) {
  const res = ref<any>({});
  return (() => {
    args.forEach((dictType: any, index: any) => {
      res.value[dictType] = [];
      const dicts = useDictStore().getDict(dictType);
      if (dicts) {
        res.value[dictType] = dicts;
      } else {
        getDict(dictType).then((resp) => {
          res.value[dictType] = resp.data.map(
            (p: {
              id: any;
              dictLabel: any;
              dictValue: any;
              listClass: any;
              iconClass: any;
            }) => ({
              id: p.id,
              label: p.dictLabel,
              value: p.dictValue,
              listClass: p.listClass,
              iconClass: p.iconClass,
            })
          );
          useDictStore().setDict(dictType, res.value[dictType]);
        });
      }
    });
    return toRefs(res.value);
  })();
}

/**
 * 通过值获取对应的名字
 * @param dictType
 * @param value
 */
export function getDictLabel(dictType: any, value: any) {
  if (value == undefined) {
    return "";
  }
  const dicts = useDictStore().getDict(dictType);
  if(dictType == 'sys_accumulate_type'){
    console.log(dicts, 'dicts')

  }
  if (dicts) {
    let info = dicts.find((item: any) => item.value == value.toString());
    return info ? info.label : "";
  } else {
    getDict(dictType).then((resp) => {
      const res = ref<any>({});
      res.value[dictType] = resp.data.map(
        (p: {
          id: any;
          dictLabel: any;
          dictValue: any;
          listClass: any;
          iconClass: any;
        }) => ({
          id: p.id,
          label: p.dictLabel,
          value: p.dictValue,
          listClass: p.listClass,
          iconClass: p.iconClass,
        })
      );
      useDictStore().setDict(dictType, res.value[dictType]);
      let info = resp.data.find(
        (item: any) => item.dictValue == value.toString()
      );
      return info ? info.dictLabel : "";
    });
  }
}

/**
 * 通过值获取对应的名字
 * @param dictType
 * @param id
 */
export function getDictValue(dictType: any, id: any) {
  if (id == undefined) {
    return "";
  }
  const dicts = useDictStore().getDict(dictType);
  if (dicts) {
    let info = dicts.find((item: any) => item.id == id.toString());
    return info ? info.value : "";
  } else {
    getDict(dictType).then((resp) => {
      const res = ref<any>({});
      res.value[dictType] = resp.data.map(
        (p: {
          id: any;
          dictLabel: any;
          dictValue: any;
          listClass: any;
          iconClass: any;
        }) => ({
          id: p.id,
          label: p.dictLabel,
          value: p.dictValue,
          listClass: p.listClass,
          iconClass: p.iconClass,
        })
      );
      useDictStore().setDict(dictType, res.value[dictType]);
      let info = resp.data.find((item: any) => item.id == id.toString());
      return info ? info.dictValue : "";
    });
  }
}
