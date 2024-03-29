import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RecipallTestModule } from '../../../test.module';
import { MealListDeleteDialogComponent } from 'app/entities/meal-list/meal-list-delete-dialog.component';
import { MealListService } from 'app/entities/meal-list/meal-list.service';

describe('Component Tests', () => {
  describe('MealList Management Delete Component', () => {
    let comp: MealListDeleteDialogComponent;
    let fixture: ComponentFixture<MealListDeleteDialogComponent>;
    let service: MealListService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RecipallTestModule],
        declarations: [MealListDeleteDialogComponent]
      })
        .overrideTemplate(MealListDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MealListDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MealListService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
