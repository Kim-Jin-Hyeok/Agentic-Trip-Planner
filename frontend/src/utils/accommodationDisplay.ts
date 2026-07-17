export type StayContextLabels = {
  stayLabel: string;
  departureLabel: string;
};

export function createStayContextLabels(stayIndex: number): StayContextLabels {
  const stayDayNo = stayIndex + 1;
  return {
    stayLabel: `Day ${stayDayNo} 숙소`,
    departureLabel: `Day ${stayDayNo + 1} 출발 일정에 반영`
  };
}
