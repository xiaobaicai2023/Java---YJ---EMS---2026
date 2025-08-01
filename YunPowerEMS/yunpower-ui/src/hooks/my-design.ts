import { Notification } from "@arco-design/web-vue";
import {isObject, isString} from "@/utils/is";

/**
 * 二次封装-消息提示框
 * @param res
 * @param content
 */
type ResponseType = { code: number, msg: string };

export const notification = (res: ResponseType | string) => {
  const type: 'success' | 'error' = isObject(res) && res.code !== 200 ? 'error' : 'success';
  const content: string = isString(res) ? res : res.msg;

  Notification[type]({
    title: '提示',
    content: content,
    duration: 2000,
  });
};

