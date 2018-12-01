export class Position {
  id: number;
  name: string;

   static toArray(jsons: any[]): Position[] {
      const positions: Position[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              positions.push(new Position(json));
          }
      }
      return positions;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name
    }
  }
}
