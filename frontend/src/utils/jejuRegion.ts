export type JejuRegion = 'EAST' | 'WEST' | 'NORTH' | 'SOUTH';

const eastAreaNames = ['구좌읍', '조천읍', '우도면', '성산읍', '표선면'];
const westAreaNames = ['애월읍', '한림읍', '한경면', '대정읍', '안덕면'];
const southAreaNames = ['남원읍'];

export function inferJejuRegion(
  address: string,
  latitude: number,
  longitude: number
): JejuRegion {
  if (includesAny(address, eastAreaNames)) {
    return 'EAST';
  }
  if (includesAny(address, westAreaNames)) {
    return 'WEST';
  }
  if (includesAny(address, southAreaNames) || address.includes('서귀포시')) {
    return 'SOUTH';
  }
  if (address.includes('제주시')) {
    return 'NORTH';
  }
  if (longitude >= 126.7) {
    return 'EAST';
  }
  if (longitude <= 126.4) {
    return 'WEST';
  }
  if (latitude <= 33.33) {
    return 'SOUTH';
  }
  return 'NORTH';
}

function includesAny(address: string, areaNames: string[]): boolean {
  return areaNames.some((areaName) => address.includes(areaName));
}
