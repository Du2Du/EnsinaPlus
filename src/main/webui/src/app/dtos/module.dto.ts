import { IResource } from "../pages/course-home/course-home.component";

export interface ModuleDTO{
    uuid: string,
    name:string,
    description: string,
    positionOrder: number,
    courseUuid: string,
    resources: IResource[]
}