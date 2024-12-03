import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductDetails from './product-details';
import ProductDetailsDetail from './product-details-detail';
import ProductDetailsUpdate from './product-details-update';
import ProductDetailsDeleteDialog from './product-details-delete-dialog';

const ProductDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductDetails />} />
    <Route path="new" element={<ProductDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<ProductDetailsDetail />} />
      <Route path="edit" element={<ProductDetailsUpdate />} />
      <Route path="delete" element={<ProductDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductDetailsRoutes;
