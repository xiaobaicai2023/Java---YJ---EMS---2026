import { AxiosResponse } from 'axios';

declare module 'axios' {
    export interface AxiosResponse<T = any> {
        code: number;
        captchaEnabled: boolean;
        img: string;
        msg: string;
        uuid: string;
        token: string;
        permissions: string[];
        roles: string[];
        user: UserInfo;
        data: T;
    }
}
