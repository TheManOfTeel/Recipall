import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRecipe } from 'app/shared/model/recipe.model';
import { RecipeService } from './recipe.service';

@Component({
  selector: 'jhi-recipe-delete-dialog',
  templateUrl: './recipe-delete-dialog.component.html'
})
export class RecipeDeleteDialogComponent {
  recipe: IRecipe;

  constructor(protected recipeService: RecipeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.recipeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'recipeListModification',
        content: 'Deleted an recipe'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-recipe-delete-popup',
  template: ''
})
export class RecipeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ recipe }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RecipeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.recipe = recipe;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/recipe', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/recipe', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
