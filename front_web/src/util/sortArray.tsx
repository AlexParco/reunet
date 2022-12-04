import { Group } from "@/types/group.type";

export const sortGroups = (groups: Group[]): Group[] => groups.sort(
  (g1, g2) =>
    (g1.id > g2.id) ? 1 : (g1.id < g2.id) ? -1 : 0
)