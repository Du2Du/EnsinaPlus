import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'spliceName',
  pure: true
})
export class SpliceNamePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) return '';
    const words = value.split(' ');
    let initials = '';
    if (words.length === 1) {
      initials = words[0].substring(0, 2).toUpperCase();
    } else {
      initials += words[0].charAt(0);
      initials += words[1].charAt(0);
      initials = initials.toUpperCase();
    }
    return initials;
  }

}
