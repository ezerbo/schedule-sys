import { Injectable } from '@angular/core';

@Injectable()
export class SpinnerService {

  constructor() { }

  private getOptions(): any {
    return {
        lines: 15,
        length: 28,
        width: 8,
        radius: 25,
        scale: 1,
        corners: 1,
        color: '#000',
        opacity: 0.1,
        rotate: 0,
        direction: 1,
        speed: 1,
        trail: 60,
        fps: 20,
        zIndex: 2e9,
        className: 'spinner',
        top: '50%',
        left: '50%',
        shadow: true,
        hwaccel: true,
        position: 'absolute'
     }
  }

}
