export function haveSameRainyDayNos(left: number[], right: number[]): boolean {
  return left.length === right.length && left.every(dayNo => right.includes(dayNo));
}

export function areForecastRainyDaysApplied(
  rainyDayMode: boolean,
  rainyDayNos: number[],
  suggestedRainyDayNos: number[]
): boolean {
  return !rainyDayMode && haveSameRainyDayNos(rainyDayNos, suggestedRainyDayNos);
}
