import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Billing from './billing';
import BillingDetail from './billing-detail';
import BillingUpdate from './billing-update';
import BillingDeleteDialog from './billing-delete-dialog';

const BillingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Billing />} />
    <Route path="new" element={<BillingUpdate />} />
    <Route path=":id">
      <Route index element={<BillingDetail />} />
      <Route path="edit" element={<BillingUpdate />} />
      <Route path="delete" element={<BillingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillingRoutes;
